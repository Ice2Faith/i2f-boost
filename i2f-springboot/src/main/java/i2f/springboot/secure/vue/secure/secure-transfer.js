import Random from "./random";
import AES from "./aes";
import Rsa from "./rsa";
import Base64Obfuscator from "./base64-obfuscator";

const SecureTransfer = {
  // 是否开启详细日志
  // 在正式环境中，请禁用
  ENABLE_DEBUG_LOG(){
    return true;
  },
  // 定义安全请求头名称
  SECURE_DATA_HEADER() {
    return "secure";
  },
  // 定义签名头名称
  SECURE_SIGN_HEADER(){
    return "sign";
  },
  // 定义一次性消息头名称
  SECURE_NONCE_HEADER(){
    return "nonce";
  },
  SECURE_SIGN_NONCE_HEADER(){
    return "sgonce";
  },
  // 启用安全请求头
  SECURE_HEADER_ENABLE(){
    return "true";
  },
  // 安全请求参数头名称
  SECURE_PARAMS_HEADER(){
    return "secure_params";
  },
  // 获取安全请求头，参数：是否启用安全参数
  getSecureHeader(openSecureParams){
    let ret={};

    return this.getSecureHeaderInto(ret,openSecureParams);
  },
  // 添加安全请求头到headers中，openSecureParams表示是否启用安全参数
  getSecureHeaderInto(headers,openSecureParams){
    headers[this.SECURE_DATA_HEADER()]=this.SECURE_HEADER_ENABLE();

    if(openSecureParams){
      headers[this.SECURE_PARAMS_HEADER()]=this.SECURE_HEADER_ENABLE();
    }
    return headers;
  },
  // 存储rsa公钥的键名
  RSA_PUBKEY_NAME() {
    return "SECURE_PUB";
  },
  // 保存rsa公钥
  saveRsaPubKey(pubKey) {
    return sessionStorage.setItem(this.RSA_PUBKEY_NAME(), pubKey);
  },
  // 加载rsa公钥
  loadRsaPubKey() {
    let pubKey = sessionStorage.getItem(this.RSA_PUBKEY_NAME());
    return Base64Obfuscator.decode(pubKey);
  },
  // 随机生成aes秘钥
  aesKeyGen(size) {
    return AES.genKey(Random.nextLowerInt(8192) + "", size);
  },
  // 随机生成16位aes秘钥
  aesKeyGen16() {
    return this.aesKeyGen(16);
  },
  // aes加密给定对象
  encrypt(obj, aesKey) {
    return AES.encryptObj(obj, aesKey);
  },
  // aes解密给定串为对象
  decrypt(bs64, aesKey) {
    return AES.decryptObj(bs64, aesKey);
  },
  // 获取安全请求头的值
  getRequestSecureHeader(aesKey) {
    if (aesKey == null || aesKey == undefined) {
      return "null";
    }
    let pubKey = this.loadRsaPubKey();
    let aesKeyTransfer = Rsa.publicKeyEncrypt(pubKey, aesKey);
    aesKeyTransfer = Base64Obfuscator.encode(aesKeyTransfer, true);
    return aesKeyTransfer;
  },
  // 直接设置安全请求头到headers中
  setRequestSecureHeader(headers, aesKey) {
    let aesKeyTransfer = this.getRequestSecureHeader(aesKey);
    headers[this.SECURE_DATA_HEADER()] = aesKeyTransfer;
  },
  // 获取安全响应头中的值
  getResponseSecureHeader(aesKeyTransfer) {
    if (aesKeyTransfer == null || aesKeyTransfer == undefined) {
      return aesKeyTransfer;
    }
    let pubKey = this.loadRsaPubKey();
    aesKeyTransfer = aesKeyTransfer.trim();
    // 解除模糊之后使用RSA进行解密得到aes秘钥
    let aesKey = Base64Obfuscator.decode(aesKeyTransfer);
    aesKey = Rsa.publicKeyDecrypt(pubKey, aesKey);
    return aesKey;
  },
  // 直接从响应头中获取安全值
  getResponseSecureHeaderByHeaders(headers) {
    let aesKeyTransfer = headers[this.SECURE_DATA_HEADER()];
    return this.getResponseSecureHeader(aesKeyTransfer);
  },

}

export default SecureTransfer;
