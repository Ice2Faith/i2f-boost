/**
 * 请求头sswh的解码组成部分
 */
const SecureHeader={
    // 签名
    sign: null,
    // 一次性消息
    nonce: null,
    // 随机秘钥
    randomKey: null,
    // RSA公钥签名
    rsaSign: null,
    newObj:()=>{
        return {
            sign: null,
            nonce: null,
            randomKey: null,
            rsaSign: null
        }
    }
}
export default SecureHeader