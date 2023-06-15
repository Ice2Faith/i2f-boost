package i2f.springboot.secure.core;


import i2f.core.digest.AESUtil;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.digest.RsaKey;
import i2f.core.digest.StringSignature;
import i2f.core.thread.NamingThreadFactory;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.util.RsaUtil;
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
 * @desc RSA+AES加密的工作支撑类
 */
@ConditionalOnBean({
        SecureConfig.class,
        JacksonJsonProcessor.class
})
@Slf4j
@Data
@Component
public class SecureTransfer implements InitializingBean {

    @Autowired
    private JacksonJsonProcessor processor;

    @Autowired
    private SecureConfig secureConfig;

    private RsaKey rsaKey;

    private LinkedList<RsaKey> histories;

    private ScheduledExecutorService pool;


    public File getRsaStoreFile(){
        File file=new File(secureConfig.getRsaStorePath(), SecureConsts.RSA_KEY_FILE_NAME);
        return file;
    }

    public void loadRsaKey(){
        File file=getRsaStoreFile();
        log.info("rsa store file:"+file.getAbsolutePath());
        if(file.exists()){
            try{
                rsaKey = RsaKey.loadRsaKey(file);
                log.info("find stored rsa key.");
            }catch(Exception e){
                log.warn("load rsa key exception:"+e.getMessage()+" of "+e.getClass().getName());
            }
        }
    }

    public void saveRsaKey(){
        File file=getRsaStoreFile();
        log.info("rsa store file:"+file.getAbsolutePath());
        try{
            RsaKey.saveRsaKey(rsaKey,file);
        }catch(Exception e){
            log.warn("save rsa key exception:"+e.getMessage() +" of "+e.getClass().getName());
        }
    }

    public void restoreRsaKey(){
        loadRsaKey();
        saveRsaKey();
    }

    public void scheduleRsaUpdate(){
        histories=new LinkedList<>();
        pool= Executors.newSingleThreadScheduledExecutor(new NamingThreadFactory("secure","refresh"));
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (SecureTransfer.this){
                    log.debug("rsaKey update....");
                    histories.addFirst(rsaKey);
                    rsaKey=RsaUtil.makeKeyPair(secureConfig.getRsaKeySize());
                    if(histories.size()> secureConfig.getDynamicMaxHistoriesCount()){
                        histories.removeLast();
                    }
                    saveRsaKey();
                }
            }
        },secureConfig.getDynamicRefreshDelaySeconds(),secureConfig.getDynamicRefreshDelaySeconds(), TimeUnit.SECONDS);
    }

    public RsaKey findRsaKey(String sign){
        if(!secureConfig.isEnableDynamicRsaKey()){
            return rsaKey;
        }
        List<RsaKey> keys=new ArrayList<>();
        keys.add(rsaKey);
        keys.addAll(histories);
        for(RsaKey item : keys){
            String b464=item.publicKeyBase64();
            String sg= StringSignature.sign(b464);
            if(sg.equalsIgnoreCase(sign)){
                return item;
            }
        }
        return null;
    }


    public String getRsaSign(){
        String b464=rsaKey.publicKeyBase64();
        String rsaSign=StringSignature.sign(b464);
        return rsaSign;
    }

    public String makeNonce(){
        return UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.rsaKey == null) {
            this.rsaKey = RsaUtil.makeKeyPair(secureConfig.getRsaKeySize());
        }
        restoreRsaKey();
        if (secureConfig.isEnableDynamicRsaKey()) {
            scheduleRsaUpdate();
        }
    }

    public String aesKeyGen(int size){
        return AESUtil.genKey(new Random().nextInt(secureConfig.getRandomKeyBound())+"",size);
    }
    public String aesKeyGen(){
        return aesKeyGen(16);
    }

    public String encrypt(Object obj,String aesKey){
        return AESUtil.encryptJsonAfterBase64(processor.serialize(obj), aesKey);
    }

    public String encryptJson(String json,String aesKey){
        return AESUtil.encryptJsonAfterBase64(json,aesKey);
    }

    public String encryptJsonBytes(byte[] json,String aesKey){
        return AESUtil.encryptJsonBytesAfterBase64(json,aesKey);
    }

    public String decrypt(String bs64,String aesKey){
        return AESUtil.decryptJsonBeforeBase64(bs64,aesKey);
    }

    public String getResponseSecureHeader(String aesKey){
        if(aesKey==null){
            return "null";
        }
        // 使用RSA对aes秘钥加密并进行模糊
        String aesKeyTransfer= RsaUtil.privateKeyEncryptBase64(rsaKey,aesKey);
        aesKeyTransfer= Base64Obfuscator.encode(aesKeyTransfer,true);
        return aesKeyTransfer;
    }

    public String getRequestSecureHeader(String aesKeyTransfer,String rsaSign){
        if(aesKeyTransfer==null){
            return aesKeyTransfer;
        }
        aesKeyTransfer=aesKeyTransfer.trim();
        RsaKey rsaKey=findRsaKey(rsaSign);
        if(rsaKey==null){
            return null;
        }
        // 解除模糊之后使用RSA进行解密得到aes秘钥
        String aesKey=Base64Obfuscator.decode(aesKeyTransfer);
        aesKey=RsaUtil.privateKeyDecryptBase64(rsaKey,aesKey);
        return aesKey;
    }

    public void setExposeHeader(ServerHttpResponse response){
        List<String> headers=new ArrayList<>();
        headers.add(secureConfig.getHeaderName());
        if(secureConfig.isEnableDynamicRsaKey()){
            headers.add(secureConfig.getDynamicKeyHeaderName());
        }
        response.getHeaders().setAccessControlExposeHeaders(headers);
    }

    public void setExposeHeader(HttpServletResponse response){

        // 将随机RSA加密模糊之后的AES秘钥放入响应头，并设置可访问权限
        Collection<String> oldHeaders = response.getHeaders(SecureConsts.ACCESS_CONTROL_EXPOSE_HEADERS);
        List<String> headers = new ArrayList<>(oldHeaders);
        headers.add(secureConfig.getHeaderName());
        if(secureConfig.isEnableDynamicRsaKey()){
            headers.add(secureConfig.getDynamicKeyHeaderName());
        }
        response.setHeader(SecureConsts.ACCESS_CONTROL_EXPOSE_HEADERS,toCommaDelimitedString(headers));
    }

    public String toCommaDelimitedString(List<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        Iterator<String> iterator = headerValues.iterator();

        while(iterator.hasNext()) {
            String val = iterator.next();
            if (val != null && !"".equals(val)) {
                joiner.add(val);
            }
        }

        return joiner.toString();
    }


    /**
     * 获取给前端的公钥
     * @return
     */
    public String getWebRsaPublicKey(){
        String pubKey=Base64Obfuscator.encode(this.getRsaKey().publicKeyBase64(),true);
        return pubKey;
    }

}
