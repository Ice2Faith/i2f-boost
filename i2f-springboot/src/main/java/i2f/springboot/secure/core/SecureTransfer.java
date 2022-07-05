package i2f.springboot.secure.core;


import i2f.core.digest.AESUtil;
import i2f.core.digest.Base64Obfuscator;
import i2f.core.digest.RsaKey;
import i2f.core.digest.StringSignature;
import i2f.core.thread.ThreadPools;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.util.RsaUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
@Slf4j
@Data
@Component
public class SecureTransfer implements InitializingBean {
    // 是否包含解密头标记
    public static final String SECURE_DATA_HEADER="secure";

    // 是否包含签名头标记
    public static final String SECURE_SIGN_HEADER="sign";

    // 是否包含一次性消息头标记
    public static final String SECURE_NONCE_HEADER="nonce";

    // 是否包含一次性签名消息头标记
    public static final String SECURE_SIGN_NONCE_HEADER="sgonce";

    // 是否包含动态刷新的RSA公钥签名/公钥头
    public static final String SECURE_RSA_PUB_KEY_OR_SIGN_HEADER="skey";

    //////////////////////////////////////////////////////////////////

    // 是否是String返回值类型标记
    public static final String STRING_RETURN_HEADER="secure_string";

    // 是否过滤器已解密标记
    public static final String FILTER_DECRYPT_HEADER="secure_decrypt";

    // 是否签名验证通过
    public static final String FILTER_SIGN_PASS_HEADER="sign_pass";

    // 是否签名验证通过
    public static final String FILTER_SIGN_NONCE_PASS_HEADER="sign_nonce_pass";

    public static final String FLAG_ENABLE ="true";

    public static final String FLAG_DISABLE ="false";

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS="Access-Control-Expose-Headers";

    public static final String RSA_KEY_FILE_NAME="rsa.key";

    // 过滤器中发生的异常，使用请求属性装载到AOP中曝出，以进行ExceptionHander的拦截
    public static final String FILTER_EXCEPTION_ATTR_KEY="secure_except";

    @Autowired
    private JacksonJsonProcessor processor;

    @Autowired
    private SecureConfig secureConfig;

    private RsaKey rsaKey= RsaUtil.makeKeyPair(secureConfig.getRsaKeySize());

    private LinkedList<RsaKey> histories;

    private ScheduledExecutorService pool;

    public File getRsaStoreFile(){
        File file=new File(secureConfig.getRsaStorePath(),RSA_KEY_FILE_NAME);
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
        pool= Executors.newSingleThreadScheduledExecutor(new ThreadPools.NamingThreadFactory("secure","refresh"));
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (SecureTransfer.this){
                    log.info("rsaKey update....");
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

    public void setRefreshRsaKey(HttpServletRequest request,HttpServletResponse response){
        if(!secureConfig.isEnableDynamicRsaKey()){
            return;
        }
        String rsaSign=request.getHeader(SECURE_RSA_PUB_KEY_OR_SIGN_HEADER);
        String b464=rsaKey.publicKeyBase64();
        String sg=StringSignature.sign(b464);
        if(!sg.equalsIgnoreCase(rsaSign)){
            response.setHeader(SECURE_RSA_PUB_KEY_OR_SIGN_HEADER,getWebRsaPublicKey());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restoreRsaKey();
        if(secureConfig.isEnableDynamicRsaKey()){
            scheduleRsaUpdate();
        }
    }

    public String aesKeyGen(int size){
        return AESUtil.genKey(new Random().nextInt(8192)+"",size);
    }
    public String aesKeyGen(){
        return aesKeyGen(16);
    }

    public String encrypt(Object obj,String aesKey){
        return AESUtil.encryptJsonAfterBase64(processor.toText(obj),aesKey);
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

    public void setResponseSecureHeader(ServerHttpResponse response, String aesKey){
        String aesKeyTransfer=getResponseSecureHeader(aesKey);

        // 将随机RSA加密模糊之后的AES秘钥放入响应头，并设置可访问权限
        List<String> headers=new ArrayList<>();
        headers.add(SECURE_DATA_HEADER);
        if(secureConfig.isEnableDynamicRsaKey()){
            headers.add(SECURE_RSA_PUB_KEY_OR_SIGN_HEADER);
        }
        response.getHeaders().setAccessControlExposeHeaders(headers);
        response.getHeaders().set(SECURE_DATA_HEADER,aesKeyTransfer);
    }

    public void setResponseSecureHeader(HttpServletResponse response, String aesKey){
        String aesKeyTransfer=getResponseSecureHeader(aesKey);

        // 将随机RSA加密模糊之后的AES秘钥放入响应头，并设置可访问权限
        Collection<String> oldHeaders = response.getHeaders(ACCESS_CONTROL_EXPOSE_HEADERS);
        List<String> headers = new ArrayList<>(oldHeaders);
        headers.add(SECURE_DATA_HEADER);
        if(secureConfig.isEnableDynamicRsaKey()){
            headers.add(SECURE_RSA_PUB_KEY_OR_SIGN_HEADER);
        }
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS,toCommaDelimitedString(headers));
        response.setHeader(SECURE_DATA_HEADER,aesKeyTransfer);
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

    public String getRequestSecureHeader(HttpServletRequest request){
        String aesKeyTransfer = request.getHeader(SECURE_DATA_HEADER);
        String rsaSign=request.getHeader(SECURE_RSA_PUB_KEY_OR_SIGN_HEADER);
        return getRequestSecureHeader(aesKeyTransfer,rsaSign);
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
