package i2f.core.secret;

import i2f.core.secret.core.SecretProvider;
import i2f.core.secret.data.Base64SecretMsg;
import i2f.core.secret.data.SecretMsg;
import i2f.core.secret.impl.ram.RamSecretProvider;
import i2f.core.secret.util.SecretUtil;

/**
 * @author ltb
 * @date 2022/10/19 18:15
 * @desc
 */
public class TestRam {
    public static void main(String[] args) throws Exception {
        SecretProvider ins = RamSecretProvider.getInstance();

        String msg = "--- ------------------------------------------------------------------\n" +
                "## 综合构建一个较为完善的安全体系\n" +
                "--- ------------------------------------------------------------------\n" +
                "- 初始化\n" +
                "\t- 生成自身的非对称秘钥对mine_pub_key,mine_pri_key\n" +
                "\t- 携带自身公钥mine_pub_key请求对方公钥other_pub_key\n" +
                "- 发送消息body\n" +
                "\t- 生成随机秘钥key=random.randomKey()【反窃听，防止固定秘钥被解密】\n" +
                "\t- 由随机秘钥key使用自身私钥mine_pri_key用rsa生成消息头random_key=rsa.encrypt(key)【消息体反窃听】\n" +
                "\t- 生成一次性消息头nonce=guid.nextGuid()【反重放】\n" +
                "\t- 使用对称加密aes用key加密body得到msg=aes.encrypt(body)【反窃听】\n" +
                "\t- 计算消息签名send_sign=md5(msg+nonce+random_key)【反篡改】\n" +
                "\t- 计算消息签名,带上数字签名属性，signature=rsa.encrypt(send_sign)【反否认】\n" +
                "\t- 发送消息：msg,signature,nonce,random_key\n" +
                "- 接受消息\n" +
                "\t- 解密发送过来的signature得到send_sign=rsa.decrypt(signature)【反否认，验证数字签名】\n" +
                "\t- 判断是否解密send_sign是否成功\n" +
                "\t\t- 如果解密失败，则数字签名验证失败，消息不合法\n" +
                "\t- 如果成功则继续\n" +
                "\t- 计算收到消息的签名recv_sign=md5(msg+nonce+random_key)【反篡改，验证消息散列】\n" +
                "\t- 比较recv_sign==send_sign\n" +
                "\t\t- 如果不相等，则丢弃消息，消息不合法\n" +
                "\t- 如果相等则继续\n" +
                "\t- 判断nonce是否已经在nonce池中【反重放，验证一次性消息】\n" +
                "\t\t- 如果已经在，则是重放请求，丢弃消息\n" +
                "\t- 如果不存在则继续\n" +
                "\t- 使用rsa用对方的公钥other_pub_key解密random_key得到key=rsa.decrypt(random_key)【反窃听】\n" +
                "\t- 使用对称加密aes用key解密消息msg得到body=aes.decrypt(msg)【反窃听】\n" +
                "\t- 处理消息体body的内容\n" +
                "\t- 处理完毕之后，按照发送消息顺序，将响应消息返回\n" +
                "- 缺陷：\n" +
                "\t- 反伪装，机制缺少，没办法防止伪装的请求\n" +
                "\t- 解决方案：\n" +
                "\t\t- 引入CA数字证书\n" +
                "\t\t- 一般来说，CA证书是归属服务器端所有\n" +
                "\t\t- 客户端下载网站的数字证书\n" +
                "\t\t- 使用数字证书颁发CA机构的公钥验证数字证书\n" +
                "\t\t- 验证通过，则消息能够确认【反伪装】";

        msg = msg + msg + msg + msg;
        byte[] data = SecretUtil.str2utf8(msg);

        byte[] zdata = SecretUtil.gzipZipBytes(data);

        byte[] ldata = SecretUtil.gzipUnzipBytes(zdata);

        boolean eq = SecretUtil.bytesCompare(data, ldata);

        SecretMsg send = ins.send(data);

        Base64SecretMsg sendWeb = send.convert();
        System.out.println(sendWeb);

        SecretMsg recvWeb = sendWeb.convert();
        System.out.println(recvWeb);

        byte[] rdata = ins.recv(recvWeb);

        String str = SecretUtil.utf82str(rdata);

        System.out.println(str);
    }
}
