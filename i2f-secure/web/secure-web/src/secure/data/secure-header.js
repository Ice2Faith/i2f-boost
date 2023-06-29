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
    // Asym公钥签名
    asymSign: null,
    newObj: () => {
        return {
            sign: null,
            nonce: null,
            randomKey: null,
            asymSign: null
        }
    }
}
export default SecureHeader