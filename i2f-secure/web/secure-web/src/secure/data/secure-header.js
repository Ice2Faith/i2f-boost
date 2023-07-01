/**
 * 请求头sswh的解码组成部分
 */
const SecureHeader={
    // 签名
    sign: null,
    // 一次性消息
    nonce: null,
    // 数字签名
    digital: null,
    // 随机秘钥
    randomKey: null,
    // 服务端非对称签名
    serverAsymSign: null,
    // 客户端非对称签名
    clientAsymSign: null,
    newObj: () => {
        return {
            sign: null,
            nonce: null,
            digital: null,
            randomKey: null,
            serverAsymSign: null,
            clientAsymSign: null
        }
    }
}
export default SecureHeader