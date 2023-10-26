package i2f.springboot.secure.core;


import i2f.core.cache.ICache;
import i2f.core.codec.CodecUtil;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    public static final String SECURE_KEY_PREFIX = "secure:key:";
    public static final String SECURE_SLF_CURR_KEY = SECURE_KEY_PREFIX + "slf:current";
    public static final String SECURE_SLF_HIS_KEY_PREFIX = SECURE_KEY_PREFIX + "slf:history:";
    public static final String SECURE_CLI_KEY_PREFIX = SECURE_KEY_PREFIX + "cli:key:";
    public static final String SECURE_CLI_KEY_IP_BIND_PREFIX = SECURE_KEY_PREFIX + "cli:ip:";
    @Autowired
    private JacksonJsonSerializer serializer;
    @Autowired
    private SecureConfig secureConfig;
    @Autowired
    private ICache<String, Object> cache;
    private ScheduledExecutorService pool;

    // 一个IP仅有一个key可以使用，也就是会剔除老的key

    private ReentrantLock lock = new ReentrantLock();

    public File getClientsAsymStoreFile() {
        File file = new File(secureConfig.getAsymStorePath(), SecureConsts.ASYM_CLIENTS_KEY_FILE_NAME);
        return file;
    }


    public File getSlfAsymStoreFile() {
        File file = new File(secureConfig.getAsymStorePath(), SecureConsts.ASYM_KEY_FILE_NAME);
        return file;
    }


    public void scheduleSlfAsymUpdate() {
        pool = Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory("secure", "refresh"));
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (SecureTransfer.this) {
                    log.debug("asymKey update....");
                    AsymKeyPair oldKey = currentSlfKey();
                    String oldSign = getAsymPubSign(oldKey);
                    cache.set(SECURE_SLF_HIS_KEY_PREFIX + oldSign, stringifyKeyPair(oldKey),
                            secureConfig.getDynamicRefreshDelaySeconds() * secureConfig.getDynamicMaxHistoriesCount(),
                            TimeUnit.SECONDS);


                    AsymKeyPair key = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
                    cache.set(SECURE_SLF_CURR_KEY, stringifyKeyPair(key));
                }
            }
        }, secureConfig.getDynamicRefreshDelaySeconds(), secureConfig.getDynamicRefreshDelaySeconds(), TimeUnit.SECONDS);
    }

    public String stringifyKeyPair(AsymKeyPair pair) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            AsymKeyPair.saveAsymKey(pair, bos);
            bos.close();
            String str = new String(bos.toByteArray());
            return str;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public AsymKeyPair parseKeyPair(String str) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
            AsymKeyPair ret = AsymKeyPair.loadAsymKey(bis);
            return ret;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public AsymKeyPair currentSlfKey() {
        Object obj = cacheGetRetry(SECURE_SLF_CURR_KEY);
        if (obj != null) {
            String str = String.valueOf(obj);
            return parseKeyPair(str);
        }
        return null;
    }

    public AsymKeyPair findSlfAsymKey(String sign) {
        if (!secureConfig.isEnableDynamicAsymKey()) {
            return currentSlfKey();
        }
        String slfSign = getSlfAsymSign();
        if (slfSign.equals(sign)) {
            return currentSlfKey();
        }
        Object obj = cacheGetRetry(SECURE_SLF_HIS_KEY_PREFIX + sign);
        if (obj != null) {
            String str = String.valueOf(obj);
            return parseKeyPair(str);
        }
        return null;
    }

    public String getSlfAsymSign() {
        return getAsymPubSign(currentSlfKey());
    }

    public String getAsymPubSign(AsymKeyPair keyPair) {
        String b464 = keyPair.getPublicKey();
        String asymSign = SignatureUtil.sign(b464);
        return asymSign;
    }

    public String getAsymPriSign(AsymKeyPair keyPair) {
        String b464 = keyPair.getPrivateKey();
        String asymSign = SignatureUtil.sign(b464);
        return asymSign;
    }

    public String makeNonce() {
        return String.format("%x", System.currentTimeMillis()) + "-" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    public void refreshClientKeyExpire(String clientAsymSign) {
        cache.expire(SECURE_CLI_KEY_PREFIX + clientAsymSign, secureConfig.getClientKeyExpireSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AsymKeyPair key = currentSlfKey();
        if (key == null) {
            key = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
            cache.set(SECURE_SLF_CURR_KEY, stringifyKeyPair(key));
        }
        if (secureConfig.isEnableDynamicAsymKey()) {
            scheduleSlfAsymUpdate();
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

    public Object cacheGetRetry(String key) {
        Object obj = null;
        int maxCount = 15;
        int curCount = 0;
        while (curCount < maxCount && obj == null) {
            if (curCount > 0) {
                try {
                    Thread.sleep(60 * curCount);
                } catch (Exception e) {

                }
            }
            obj = cache.get(key);
            curCount++;
        }
        if (curCount > 1) {
            log.warn("cache-retry,curCount=" + curCount + ",isNull=" + (obj == null));
        } else {
            log.info("cache-retry,curCount=" + curCount + ",isNull=" + (obj == null));
        }
        return obj;
    }

    public AsymKeyPair clientKeyPair(String clientAsymSign) {
        Object obj = cacheGetRetry(SECURE_CLI_KEY_PREFIX + clientAsymSign);
        if (obj != null) {
            String str = String.valueOf(obj);
            return parseKeyPair(str);
        }
        return null;
    }

    public String getResponseSecureHeader(String symmKey, String clientAsymSign) {
        if (symmKey == null) {
            return "null";
        }
        // 使用Asym对symm秘钥加密并进行模糊
        AsymKeyPair keyPair = clientKeyPair(clientAsymSign);
        String symmKeyTransfer = AsymmetricUtil.publicKeyEncryptBase64(keyPair, symmKey);
        symmKeyTransfer = Base64Obfuscator.encode(symmKeyTransfer, true);
        return symmKeyTransfer;
    }

    public String makeDigitalSign(String symmKey) {
        if (symmKey == null) {
            return "null";
        }
        AsymKeyPair keyPair = currentSlfKey();
        String symmKeyTransfer = AsymmetricUtil.makeSignAsString(keyPair, CodecUtil.toUtf8(symmKey));
        symmKeyTransfer = Base64Obfuscator.encode(symmKeyTransfer, true);
        return symmKeyTransfer;
    }

    public String getRequestSecureHeader(String symmKeyTransfer, String asymSign) {
        if (symmKeyTransfer == null) {
            return symmKeyTransfer;
        }
        symmKeyTransfer = symmKeyTransfer.trim();
        AsymKeyPair asymKey = findSlfAsymKey(asymSign);
        if (asymKey == null) {
            return null;
        }
        // 解除模糊之后使用Asym进行解密得到symm秘钥
        String symmKey = Base64Obfuscator.decode(symmKeyTransfer);
        symmKey = AsymmetricUtil.privateKeyDecryptBase64(asymKey, symmKey);
        return symmKey;
    }

    public boolean verifyDigitalSign(String symmKeyTransfer, String sign, String asymSign) {
        if (symmKeyTransfer == null) {
            return false;
        }
        symmKeyTransfer = symmKeyTransfer.trim();
        AsymKeyPair asymKey = clientKeyPair(asymSign);
        if (asymKey == null) {
            return false;
        }
        // 解除模糊之后使用Asym进行解密得到symm秘钥
        String symmKey = Base64Obfuscator.decode(symmKeyTransfer);
        return AsymmetricUtil.verifySignByString(asymKey, symmKey, CodecUtil.toUtf8(sign));
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
        Set<String> headers = new LinkedHashSet<>();
        for (String header : oldHeaders) {
            String[] arr = header.split(",");
            for (String item : arr) {
                String str = item.trim();
                if (!StringUtils.isEmpty(str)) {
                    headers.add(str);
                }
            }
        }
        headers.add(secureConfig.getHeaderName());
        if (secureConfig.isEnableDynamicAsymKey()) {
            headers.add(secureConfig.getDynamicKeyHeaderName());
        }
        headers.add(secureConfig.getClientKeyHeaderName());
        response.setHeader(SecureConsts.ACCESS_CONTROL_EXPOSE_HEADERS, toCommaDelimitedString(headers));
    }

    public String toCommaDelimitedString(Collection<String> headerValues) {
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
        String pubKey = Base64Obfuscator.encode(currentSlfKey().getPublicKey(), true);
        return pubKey;
    }

    public String getClientAsymSignCacheKey(String priSign, String clientIp) {
        if (clientIp != null) {
            clientIp = clientIp.replaceAll(":", "-");
        }
        return clientIp + ":" + priSign;
    }

    public String getWebClientAsymPrivateKey(HttpServletRequest request) {
        AsymKeyPair keyPair = null;

        String clientIp = ServletContextUtil.getIp(request);
        if (clientIp != null) {
            clientIp = clientIp.replaceAll(":", "-");
        }

        if (clientIp != null) {
            Object obj = cacheGetRetry(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp);
            if (obj != null) {
                String skey = String.valueOf(obj);
                keyPair = clientKeyPair(skey);
            }
        }

        if (keyPair == null) {
            keyPair = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
        }

        String clientAsymSignOrigin = ServletContextUtil.getPossibleValue(secureConfig.getClientAsymSignName(), request);
        if (StringUtils.isEmpty(clientAsymSignOrigin)) {
            clientAsymSignOrigin = "";
        }
        String clientAsymSign = clientAsymSignOrigin;
        if (secureConfig.isEnableClientIpBind()) {
            clientAsymSign = getClientAsymSignCacheKey(clientAsymSignOrigin, clientIp);
        }

        String pubSign = getAsymPubSign(keyPair);
        String clientPubSign = pubSign;
        if (secureConfig.isEnableClientIpBind()) {
            pubSign = getClientAsymSignCacheKey(pubSign, clientIp);
        }
        lock.lock();
        try {
            cache.remove(SECURE_CLI_KEY_PREFIX + clientAsymSign);

            Object obj = cacheGetRetry(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp);
            if (obj != null) {
                String ckey = String.valueOf(obj);
                cache.remove(SECURE_CLI_KEY_PREFIX + ckey);
            }
            cache.remove(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp);

            cache.set(SECURE_CLI_KEY_PREFIX + pubSign, stringifyKeyPair(keyPair), secureConfig.getClientKeyExpireSeconds(), TimeUnit.SECONDS);
            cache.set(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp, pubSign);
        } finally {
            lock.unlock();
        }

        String priKey = Base64Obfuscator.encode(keyPair.getPrivateKey(), true);
        return clientPubSign + secureConfig.getHeaderSeparator() + priKey;
    }

    public String getWebAsymPublicKeyAndSwap(HttpServletRequest request, String clientKey) throws Exception {
        String publicKey = Base64Obfuscator.decode(clientKey);

        AsymKeyPair keyPair = null;
        if (publicKey != null) {
            keyPair = new AsymKeyPair(publicKey, null);
        }

        String clientIp = ServletContextUtil.getIp(request);
        if (clientIp != null) {
            clientIp = clientIp.replaceAll(":", "-");
        }

        String clientAsymSignOrigin = ServletContextUtil.getPossibleValue(secureConfig.getClientAsymSignName(), request);
        if (StringUtils.isEmpty(clientAsymSignOrigin)) {
            clientAsymSignOrigin = "";
        }
        String clientAsymSign = clientAsymSignOrigin;
        if (secureConfig.isEnableClientIpBind()) {
            clientAsymSign = getClientAsymSignCacheKey(clientAsymSignOrigin, clientIp);
        }

        String pubSign = getAsymPubSign(keyPair);
        if (secureConfig.isEnableClientIpBind()) {
            pubSign = getClientAsymSignCacheKey(pubSign, clientIp);
        }
        lock.lock();
        try {
            cache.remove(SECURE_CLI_KEY_PREFIX + clientAsymSign);

            Object obj = cacheGetRetry(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp);
            if (obj != null) {
                String ckey = String.valueOf(obj);
                cache.remove(SECURE_CLI_KEY_PREFIX + ckey);
            }
            cache.remove(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp);

            cache.set(SECURE_CLI_KEY_PREFIX + pubSign, stringifyKeyPair(keyPair), secureConfig.getClientKeyExpireSeconds(), TimeUnit.SECONDS);
            cache.set(SECURE_CLI_KEY_IP_BIND_PREFIX + clientIp, pubSign);
        } finally {
            lock.unlock();
        }

        String pubKey = Base64Obfuscator.encode(currentSlfKey().getPublicKey(), true);
        return pubKey;
    }
}
