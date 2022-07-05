import SecureTransfer from "./secure-transfer";
import StringSignature from "./string-signature";
import B64 from "./base64";

const SecureTransferFilter = {
  // 请求加密过滤，config需要包含属性：data,params,headers
  // 分别对应请求体，请求参数，请求头
  // 根据请求头中的SECURE_DATA_HEADER头值为SECURE_HEADER_ENABLE时，进行自动加密请求体
  // 如果同时请求头SECURE_PARAMS_HEADER值为SECURE_HEADER_ENABLE时，请求参数也会被加密
  // 也就是说，如果请求头中，这两个请求头不存在，或者值不为SECURE_HEADER_ENABLE，都将不会处理
  // 也就是手动处理，部分参数的情形
  requestFilter(config) {
    if (config.headers[SecureTransfer.SECURE_DATA_HEADER()]===SecureTransfer.SECURE_HEADER_ENABLE()) {
      let aesKey = SecureTransfer.aesKeyGen16();
      SecureTransfer.setRequestSecureHeader(config.headers, aesKey);
      if (config.data) {
        config.data = SecureTransfer.encrypt(config.data, aesKey);

        // 生成一次性头，保证每一次都是客户端的请求，而不是被重放的请求（实际重放看signNonce）
        let nonce=new Date().getTime().toString(16)+''+Math.floor(Math.random()*0x0fff).toString(16);

        // 计算消息体签名，保证消息体不被篡改
        let sign=StringSignature.sign(config.data)

        // 计算签名与一次性头的复合签名，保证消息体绑定的一次性头不被篡改
        let signNonceText=nonce+sign
        let signNonce=StringSignature.sign(signNonceText)

        // 将三个复合签名都添加到请求头
        config.headers[SecureTransfer.SECURE_SIGN_HEADER()]=sign
        config.headers[SecureTransfer.SECURE_NONCE_HEADER()]=nonce
        config.headers[SecureTransfer.SECURE_SIGN_NONCE_HEADER()]=signNonce

        let pubKey=SecureTransfer.loadRsaPubKey();
        let skey=StringSignature.sign(pubKey);
        config.headers[SecureTransfer.SECURE_RSA_PUB_KEY_OR_SIGN_HEADER()]=skey;
      }
      if(config.headers[SecureTransfer.SECURE_PARAMS_HEADER()]===SecureTransfer.SECURE_HEADER_ENABLE()){
        if(config.params){
          for (const propName of Object.keys(config.params)) {
            const value = config.params[propName];
            if (value !== null && typeof(value) !== "undefined") {
              if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                  value[key]=SecureTransfer.encrypt(value[key],aesKey);
                }
              } else {
                config.params[propName]=SecureTransfer.encrypt(config.params[propName],aesKey);
              }
            }
          }
        }
      }
    }
    return config;
  },
  // 响应解密过滤，res需要包含headers和data
  // 分别表示响应头和响应体
  // 当响应头中存在SECURE_DATA_HEADER时，将会自动解密响应体
  responseFilter(res) {
    let skeyHeader=res.headers[SecureTransfer.SECURE_RSA_PUB_KEY_OR_SIGN_HEADER()];
    if(skeyHeader && skeyHeader!=''){
      SecureTransfer.saveRsaPubKey(skeyHeader);
    }
    let secureHeader = res.headers[SecureTransfer.SECURE_DATA_HEADER()];
    if (secureHeader && secureHeader != '') {
      let aesKey = SecureTransfer.getResponseSecureHeaderByHeaders(res.headers);

      res.data = SecureTransfer.decrypt(res.data, aesKey);

    }
    return res;
  },
}


export default SecureTransferFilter;
