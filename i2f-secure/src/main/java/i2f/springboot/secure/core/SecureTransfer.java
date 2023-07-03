package i2f.springboot.secure.core;


import i2f.core.digest.AsymmetricKeyPair;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.io.stream.StreamUtil;
import i2f.core.j2ee.web.ServletContextUtil;
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
import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    @Autowired
    private JacksonJsonSerializer serializer;

    @Autowired
    private SecureConfig secureConfig;

    private AsymmetricKeyPair asymKey;

    private LinkedList<AsymmetricKeyPair> histories;

    private ScheduledExecutorService pool;

    // 一个IP仅有一个key可以使用，也就是会剔除老的key
    private ConcurrentHashMap<String, AsymmetricKeyPair> clientKeys = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> keyExpireMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> clientIpKeyMap = new ConcurrentHashMap<>();
    private ReentrantLock lock = new ReentrantLock();
    private ScheduledExecutorService expirePool = Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory("secure", "expire"));

    public File getClientsAsymStoreFile() {
        File file = new File(secureConfig.getAsymStorePath(), SecureConsts.ASYM_CLIENTS_KEY_FILE_NAME);
        return file;
    }

    public void loadClientsAsymKeys() {
        try {
            File storeFile = getClientsAsymStoreFile();
            if (!storeFile.exists()) {
                return;
            }
            String str = StreamUtil.readString(storeFile, "UTF-8");
            String[] lines = str.split("\n");
            Map<String, Long> expireMap = new HashMap<>();
            Map<String, AsymmetricKeyPair> keyMap = new HashMap<>();
            Map<String,String> ipMap=new HashMap<>();
            for (String line : lines) {
                String[] arr = line.split("\\|");
                if (arr.length < 1) {
                    continue;
                }
                String type = arr[0];
                if ("0".equals(type)) {
                    if (arr.length < 3) {
                        continue;
                    }
                    try {
                        long ts = Long.parseLong(arr[2]);
                        expireMap.put(arr[1], ts);
                    } catch (Exception e) {
                        log.warn("load clients asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
                    }
                }
                if ("2".equals(type)) {
                    if (arr.length < 3) {
                        continue;
                    }
                    ipMap.put(arr[1], arr[2]);
                }
                if ("1".equals(type)) {
                    if (arr.length < 4) {
                        continue;
                    }
                    try {
                        String k = arr[1];
                        String pubk = arr[2];
                        String prik = arr[3];
                        PublicKey publicKey = AsymmetricKeyPair.parsePublicKeyBase64(pubk);
                        PrivateKey privateKey = AsymmetricKeyPair.parsePrivateKeyBase64(prik);
                        AsymmetricKeyPair keyPair = new AsymmetricKeyPair(new KeyPair(publicKey, privateKey));
                        keyMap.put(k, keyPair);
                    } catch (Exception e) {
                        log.warn("load clients asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
                    }
                }
            }
            keyExpireMap.putAll(expireMap);
            clientKeys.putAll(keyMap);
            clientIpKeyMap.putAll(ipMap);
        } catch (Exception e) {
            log.warn("load clients asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
        }
    }

    public void saveClientsAsymKeys() {
        try {
            File storeFile = getClientsAsymStoreFile();
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Long> entry : keyExpireMap.entrySet()) {
                builder.append("0").append("|").append(entry.getKey()).append("|").append(entry.getValue()).append("\n");
            }
            for (Map.Entry<String, String> entry : clientIpKeyMap.entrySet()) {
                builder.append("2").append("|").append(entry.getKey()).append("|").append(entry.getValue()).append("\n");
            }
            for (Map.Entry<String, AsymmetricKeyPair> entry : clientKeys.entrySet()) {
                AsymmetricKeyPair keyPair = entry.getValue();
                builder.append("1").append("|").append(entry.getKey()).append("|").append(keyPair.publicKeyBase64()).append("|").append(keyPair.privateKeyBase64()).append("\n");
            }

            String str = builder.toString();
            StreamUtil.writeString(str, "UTF-8", storeFile);
        } catch (Exception e) {
            log.warn("save clients asym key exception:" + e.getMessage() + " of " + e.getClass().getName());
        }
    }


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

    public String getAsymPriSign(AsymmetricKeyPair keyPair) {
        String b464 = keyPair.privateKeyBase64();
        String asymSign = SignatureUtil.sign(b464);
        return asymSign;
    }

    public String makeNonce() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public void scheduleExpireKeys() {
        expirePool.scheduleWithFixedDelay(() -> {
            Set<String> rmSet = new HashSet<>();
            long ts = System.currentTimeMillis();
            lock.lock();
            try {
                for (Map.Entry<String, Long> entry : keyExpireMap.entrySet()) {
                    if (ts > entry.getValue()) {
                        rmSet.add(entry.getKey());
                    }
                }
                for (String key : clientKeys.keySet()) {
                    if (!keyExpireMap.containsKey(key)) {
                        rmSet.add(key);
                    }
                }
                for (String key : keyExpireMap.keySet()) {
                    if (!clientKeys.containsKey(key)) {
                        rmSet.add(key);
                    }
                }
                for (String key : rmSet) {
                    keyExpireMap.remove(key);
                    clientKeys.remove(key);
                }
            } finally {
                lock.unlock();
            }
            rmSet.clear();
            for (Map.Entry<String, String> entry : clientIpKeyMap.entrySet()) {
                if(!clientKeys.containsKey(entry.getValue())){
                    rmSet.add(entry.getKey());
                }
            }
            for (String key : rmSet) {
                clientIpKeyMap.remove(key);
            }
            saveClientsAsymKeys();
        }, 0, secureConfig.getClientKeyExpirePoolDelaySeconds(), TimeUnit.SECONDS);
    }

    public void refreshClientKeyExpire(String clientAsymSign) {
        keyExpireMap.put(clientAsymSign, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(secureConfig.getClientKeyExpireSeconds()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadAsymKey();
        if (this.asymKey == null) {
            this.asymKey = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());
        }
        restoreAsymKey();
        if (secureConfig.isEnableDynamicAsymKey()) {
            scheduleAsymUpdate();
        }
        loadClientsAsymKeys();
        scheduleExpireKeys();
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

    public String getResponseSecureHeader(String symmKey, String clientAsymSign) {
        if (symmKey == null) {
            return "null";
        }
        // 使用Asym对symm秘钥加密并进行模糊
        AsymmetricKeyPair keyPair = clientKeys.get(clientAsymSign);
        String symmKeyTransfer = AsymmetricUtil.publicKeyEncryptBase64(keyPair, symmKey);
        symmKeyTransfer = Base64Obfuscator.encode(symmKeyTransfer, true);
        return symmKeyTransfer;
    }

    public String getResponseDigitalHeader(String symmKey) {
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

    public String getRequestDigitalHeader(String symmKeyTransfer, String asymSign) {
        if (symmKeyTransfer == null) {
            return symmKeyTransfer;
        }
        symmKeyTransfer = symmKeyTransfer.trim();
        AsymmetricKeyPair asymKey = clientKeys.get(asymSign);
        if (asymKey == null) {
            return null;
        }
        // 解除模糊之后使用Asym进行解密得到symm秘钥
        String symmKey = Base64Obfuscator.decode(symmKeyTransfer);
        symmKey = AsymmetricUtil.publicKeyDecryptBase64(asymKey, symmKey);
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

    public String getClientAsymSignCacheKey(String priSign, String clientIp) {
        return clientIp + ";" + priSign;
    }

    public String getWebClientAsymPrivateKey(HttpServletRequest request) {
        AsymmetricKeyPair keyPair = AsymmetricUtil.makeKeyPair(secureConfig.getAsymKeySize());

        String clientIp = ServletContextUtil.getIp(request);

        String clientAsymSignOrigin = ServletContextUtil.getPossibleValue(secureConfig.getClientAsymSignName(), request);
        if(StringUtils.isEmpty(clientAsymSignOrigin)){
            clientAsymSignOrigin="";
        }
        String clientAsymSign=clientAsymSignOrigin;
        if(secureConfig.isEnableClientIpBind()){
            clientAsymSign=getClientAsymSignCacheKey(clientAsymSignOrigin,clientIp);
        }

        String priSign = getAsymPriSign(keyPair);
        if (secureConfig.isEnableClientIpBind()) {
            priSign = getClientAsymSignCacheKey(priSign, clientIp);
        }
        lock.lock();
        try {
            clientKeys.remove(clientAsymSign);
            keyExpireMap.remove(clientAsymSign);
            if (clientIpKeyMap.containsKey(clientIp)) {
                String ckey = clientIpKeyMap.get(clientIp);
                clientKeys.remove(ckey);
                keyExpireMap.remove(ckey);
            }
            clientKeys.put(priSign, keyPair);
            clientIpKeyMap.put(clientIp,priSign);
            refreshClientKeyExpire(priSign);
        } finally {
            lock.unlock();
        }
        saveClientsAsymKeys();

        String priKey = Base64Obfuscator.encode(keyPair.privateKeyBase64(), true);
        return priKey;
    }

}
