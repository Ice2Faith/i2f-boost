/**
 * 核心处理逻辑类
 */
import Base64Obfuscator from "../util/base64-obfuscator";
import SecureConsts from "../consts/secure-consts";
import StringUtils from "../util/string-utils";
import SignatureUtil from "../crypto/SignatureUtil";
import AsymmetricUtil from "../crypto/AsymmetricUtil";
import SymmetricUtil from "../crypto/SymmetricUtil";

const SecureTransfer = {
  // 获取安全请求头，参数：是否启用安全参数，是否编码URL
  getSecureHeader(openSecureParams, openSecureUrl) {
    let ret = {};

    return this.getSecureHeaderInto(ret, openSecureParams, openSecureUrl);
  },
  // 添加安全请求头到headers中，openSecureParams表示是否启用安全参数，openSecureUrl表示是否启用安全URL编码
  getSecureHeaderInto(headers, openSecureParams, openSecureUrl) {
    headers[SecureConsts.SECURE_DATA_HEADER()]=SecureConsts.FLAG_ENABLE();

    if(openSecureParams){
      headers[SecureConsts.SECURE_PARAMS_HEADER()]=SecureConsts.FLAG_ENABLE();
    }
    if(openSecureUrl){
      headers[SecureConsts.SECURE_URL_HEADER()]=SecureConsts.FLAG_ENABLE();
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
    return SymmetricUtil.genKey(size);
  },
  // 随机生成16位aes秘钥
  aesKeyGen16() {
    return this.aesKeyGen(16);
  },
  // aes加密给定对象
  encrypt(obj, aesKey) {
    return SymmetricUtil.encryptObj(obj, aesKey);
  },
  // aes解密给定串为对象
  decrypt(bs64, aesKey) {
    return SymmetricUtil.decryptObj(bs64, aesKey);
  },
  // 获取RSA公钥签名
  getRsaSign(){
    let b464 = this.loadRsaPubKey();
    let rsaSign = SignatureUtil.sign(b464);
    return rsaSign;
  },
  // 获取安全请求头的值
  getRequestSecureHeader(aesKey) {
    if (StringUtils.isEmpty(aesKey)) {
      return "null";
    }
    let pubKey = this.loadRsaPubKey();
    let aesKeyTransfer = AsymmetricUtil.publicKeyEncrypt(pubKey, aesKey);
    aesKeyTransfer = Base64Obfuscator.encode(aesKeyTransfer, true);
    return aesKeyTransfer;
  },
  // 获取安全响应头中的值
  getResponseSecureHeader(aesKeyTransfer) {
    if (StringUtils.isEmpty(aesKeyTransfer)) {
      return null;
    }
    let pubKey = this.loadRsaPubKey();
    aesKeyTransfer = aesKeyTransfer.trim();
    // 解除模糊之后使用RSA进行解密得到aes秘钥
    let aesKey = Base64Obfuscator.decode(aesKeyTransfer);
    aesKey = AsymmetricUtil.publicKeyDecrypt(pubKey, aesKey);
    return aesKey;
  },


}

export default SecureTransfer;
