package test.jce;

import i2f.core.security.jce.codec.CodecUtil;
import i2f.core.security.jce.digest.MessageDigestUtil;
import i2f.core.security.jce.digest.md.MdMessageDigester;
import i2f.core.security.jce.digest.md.MdType;
import i2f.core.security.jce.digest.sha.ShaMessageDigester;
import i2f.core.security.jce.digest.sha.ShaType;
import i2f.core.security.jce.digest.std.IMessageDigester;
import i2f.core.security.jce.encrypt.EncryptUtil;

/**
 * @author ltb
 * @date 2022/6/9 8:35
 * @desc
 */
public class TestMessageDigest {
    public static void main(String[] args) throws Exception {
        String str="网上有说明文长度小于等于密钥长度（Bytes）-11，这说法自己不太准确，会给人感受RSA 1024只能加密117字节长度明文。实际上，RSA算法自己要求加密内容也就是明文长度m必须0<m<n，也就是说内容这个大整数不能超过n，不然就出错。那么若是m=0是什么结果？广泛RSA加密器会直接返回全0结果。若是m>n，运算就会出错？！那怎么办？且听下文分解。htm\n" +
                "\n" +
                "因此，RSA实际可加密的明文长度最大也是1024bits，但问题就来了：\n" +
                "\n" +
                "若是小于这个长度怎么办？就须要进行padding，由于若是没有padding，用户没法确分解密后内容的真实长度，字符串之类的内容问题还不大，以0做为结束符，但对二进制数据就很难理解，由于不肯定后面的0是内容仍是内容结束符。\n" +
                "\n" +
                "只要用到padding，那么就要占用实际的明文长度，因而才有117字节的说法。咱们通常使用的padding标准有NoPPadding、OAEPPadding、PKCS1Padding等，其中PKCS#1建议的padding就占用了11个字节。\n" +
                "\n" +
                "若是大于这个长度怎么办？不少算法的padding每每是在后边的，但PKCS的padding则是在前面的，此为有意设计，有意的把第一个字节置0以确保m的值小于n。\n" +
                "\n" +
                "这样，128字节（1024bits）-减去11字节正好是117字节，但对于RSA加密来说，padding也是参与加密的，因此，依然按照1024bits去理解，但实际的明文只有117字节了。\n" +
                "\n" +
                "关于PKCS#1 padding规范可参考：RFC2313 chapter 8.1，咱们在把明文送给RSA加密器前，要确认这个值是否是大于n，也就是若是接近n位长，那么须要先padding再分段加密。除非咱们是“定长定量本身可控可理解”的加密不须要padding。";

        byte[] data = EncryptUtil.aesKgen("hello".getBytes()).encrypt("hello".getBytes());
        System.out.println(CodecUtil.toHexString(data, ",0x"));

        byte[] mdd = MessageDigestUtil.sha(ShaType.SHA384).mds("hello".getBytes());
        System.out.println(CodecUtil.toHexString(mdd));

        IMessageDigester digestor = new MdMessageDigester();
        doTest(digestor,str);

        digestor = new MdMessageDigester(MdType.MD2);
        doTest(digestor, str);

        digestor = new ShaMessageDigester();
        doTest(digestor, str);

        digestor = new ShaMessageDigester(ShaType.SHA224);
        doTest(digestor, str);

        digestor = new ShaMessageDigester(ShaType.SHA256);
        doTest(digestor, str);

        digestor = new ShaMessageDigester(ShaType.SHA384);
        doTest(digestor, str);

        digestor = new ShaMessageDigester(ShaType.SHA512);
        doTest(digestor, str);
    }

    public static void doTest(IMessageDigester digestor, String str) throws Exception {
        try {
            System.out.println("-----------------------------------");
            System.out.println("enc:" + digestor.getClass().getName());
            System.out.println("src:" + str);
            byte[] sdata = str.getBytes();
            System.out.println("sdata:" + CodecUtil.toHexString(sdata, ",0x"));
            byte[] edata = digestor.mds(sdata);
            String rstr = CodecUtil.toHexString(edata);
            System.out.println("dst:" + rstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
