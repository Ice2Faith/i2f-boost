package i2f.springboot.secure.core;


import i2f.core.digest.AsymmetricKeyPair;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.thread.NamingThreadFactory;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.crypto.AsymmetricUtil;
import i2f.springboot.secure.crypto.SignatureUtil;
import i2f.springboot.secure.crypto.SymmetricUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/6/29 9:04
 * @desc Asym+Symm加密的工作支撑类
 */
@ConditionalOnBean({
        SecureConfig.class,
        JacksonJsonSerializer.class
})
@Slf4j
@Data
@Component
public class SecureTransfer implements InitializingBean {

    @Autowired
    private JacksonJsonSerializer serializer;

    @Autowired
    private SecureConfig secureConfig;

    private AsymmetricKeyPair asymKey;

    private LinkedList<AsymmetricKeyPair> histories;

    private ScheduledExecutorService pool;


    public File getAsymStoreFile() {
        File file = new File(secureConfig.getAsymStorePath(), SecureConsts.ASYM_KEY_FILE_NAME);
        return file;
    }

    public void loadAsymKey() {
        File file = getAsymStoreFile();
        log.info("asymKey store file:" + file.getAbsolutePath());
        if (file.exists()) {
            try {
                asymKey = AsymmetricKeyPair.loadAsymKey(file);
                log.info("find stored asym key.");
            } catch (Exception e) {
                log.warn("load asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
            }
        }
    }

    public void saveAsymKey() {
        File file = getAsymStoreFile();
        log.info("asym store file:" + file.getAbsolutePath());
        try {
            AsymmetricKeyPair.saveAsymKey(asymKey, file);
        } catch (Exception e) {
            log.warn("save asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
        }
    }

    public void restoreAsymKey() {
        loadAsymKey();
        saveAsymKey();
    }

    public void scheduleAsymUpdate() {
        histories = new LinkedList<>();
        pool = Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory("secure", "refresh"));
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (SecureTransfer.this) {
                    log.debug("asymKey update....");
                    histories.addFirst(asymKey);
                    asymKey = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
                    if (histories.size() > secureConfig.getDynamicMaxHistoriesCount()) {
                        histories.removeLast();
                    }
                    saveAsymKey();
                }
            }
        }, secureConfig.getDynamicRefreshDelaySeconds(), secureConfig.getDynamicRefreshDelaySeconds(), TimeUnit.SECONDS);
    }

    public AsymmetricKeyPair findAsymKey(String sign) {
        if (!secureConfig.isEnableDynamicAsymKey()) {
            return asymKey;
        }
        List<AsymmetricKeyPair> keys = new ArrayList<>();
        keys.add(asymKey);
        keys.addAll(histories);
        for (AsymmetricKeyPair item : keys) {
            String b464 = item.publicKeyBase64();
            String sg = SignatureUtil.sign(b464);
            if (sg.equalsIgnoreCase(sign)) {
                return item;
            }
        }
        return null;
    }


    public String getAsymSign() {
        String b464 = asymKey.publicKeyBase64();
        String asymSign = SignatureUtil.sign(b464);
        return asymSign;
    }

    public String makeNonce() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.asymKey == null) {
            this.asymKey = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
        }
        restoreAsymKey();
        if (secureConfig.isEnableDynamicAsymKey()) {
            scheduleAsymUpdate();
        }
    }

    public String symmetricKeyGen(int size) {
        return SymmetricUtil.genKey(size);
    }

    public String symmetricKeyGen() {
        return symmetricKeyGen(32);
    }

    public String encrypt(Object obj, String symmKey) {
        return SymmetricUtil.encryptJsonAfterBase64(serializer.serialize(obj), symmKey);
    }

    public String encryptJson(String json, String symmKey) {
        return SymmetricUtil.encryptJsonAfterBase64(json, symmKey);
    }

    public String encryptJsonBytes(byte[] json, String symmKey) {
        return SymmetricUtil.encryptJsonBytesAfterBase64(json, symmKey);
    }

    public String decrypt(String bs64, String symmKey) {
        return SymmetricUtil.decryptJsonBeforeBase64(bs64, symmKey);
    }

    public String getResponseSecureHeader(String symmKey) {
        if (symmKey == null) {
            return "null";
        }
        // 使用Asym对symm秘钥加密并进行模糊
        String symmKeyTransfer = AsymmetricUtil.privateKeyEncryptBase64(asymKey, symmKey);
        symmKeyTransfer = Base64Obfuscator.encode(symmKeyTransfer, true);
        return symmKeyTransfer;
    }

    public String getRequestSecureHeader(String symmKeyTransfer, String asymSign) {
        if (symmKeyTransfer == null) {
            return symmKeyTransfer;
        }
        symmKeyTransfer = symmKeyTransfer.trim();
        AsymmetricKeyPair asymKey = findAsymKey(asymSign);
        if (asymKey == null) {
            return null;
        }
        // 解除模糊之后使用Asym进行解密得到symm秘钥
        String symmKey = Base64Obfuscator.decode(symmKeyTransfer);
        symmKey = AsymmetricUtil.privateKeyDecryptBase64(asymKey, symmKey);
        return symmKey;
    }

    public void setExposeHeader(ServerHttpResponse response) {
        List<String> headers = new ArrayList<>();
        headers.add(secureConfig.getHeaderName());
        if (secureConfig.isEnableDynamicAsymKey()) {
            headers.add(secureConfig.getDynamicKeyHeaderName());
        }
        response.getHeaders().setAccessControlExposeHeaders(headers);
    }

    public void setExposeHeader(HttpServletResponse response) {

        // 将随机Asym加密模糊之后的Symm秘钥放入响应头，并设置可访问权限
        Collection<String> oldHeaders = response.getHeaders(SecureConsts.ACCESS_CONTROL_EXPOSE_HEADERS);
        List<String> headers = new ArrayList<>(oldHeaders);
        headers.add(secureConfig.getHeaderName());
        if (secureConfig.isEnableDynamicAsymKey()) {
            headers.add(secureConfig.getDynamicKeyHeaderName());
        }
        response.setHeader(SecureConsts.ACCESS_CONTROL_EXPOSE_HEADERS, toCommaDelimitedString(headers));
    }

    public String toCommaDelimitedString(List<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        Iterator<String> iterator = headerValues.iterator();

        while (iterator.hasNext()) {
            String val = iterator.next();
            if (val != null && !"".equals(val)) {
                joiner.add(val);
            }
        }

        return joiner.toString();
    }


    /**
     * 获取给前端的公钥
     *
     * @return
     */
    public String getWebAsymPublicKey() {
        String pubKey = Base64Obfuscator.encode(this.getAsymKey().publicKeyBase64(), true);
        return pubKey;
    }

}
