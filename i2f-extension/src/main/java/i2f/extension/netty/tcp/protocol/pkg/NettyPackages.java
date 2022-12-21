package i2f.extension.netty.tcp.protocol.pkg;

import i2f.extension.netty.tcp.protocol.consts.NettyConst;
import i2f.extension.netty.tcp.protocol.consts.NettyFlag;
import i2f.extension.netty.tcp.protocol.consts.NettySerializeType;

import java.io.*;

/**
 * 提供了预定义的序列化类型和包生成和解析
 * 分别是of系列和parse系列
 * 分别作为生成包和解析包两部分
 * 除此之外，还提供一些默认的包，例如：心跳包
 */
public class NettyPackages {
    public static final NettyPackage HEART_BEAT_PKG;

    static {
        NettyPackage pkg = instance();
        pkg.flag = (byte) NettyFlag.HEART_BEAT.code();
        HEART_BEAT_PKG = pkg;
    }

    public static NettyPackage instance() {
        NettyPackage ret = new NettyPackage();
        ret.magicNumber = NettyConst.MAGIC_NUMBER;
        ret.version = NettyConst.PKG_VERSION;
        ret.serializeType = (byte) NettySerializeType.BIN.code();
        ret.pkgType = 0;
        ret.flag = (byte) NettyFlag.NONE.code();
        ret.seqId = 0;
        ret.length = 0;
        ret.content = null;
        return ret;
    }

    public static NettyPackage of(byte[] content, byte serializeType) {
        NettyPackage ret = instance();
        ret.serializeType = serializeType;
        ret.length = content.length;
        ret.content = content;
        return ret;
    }

    public static NettyPackage ofBin(byte[] content) {
        return of(content, (byte) NettySerializeType.BIN.code());
    }

    public static <T extends Serializable> NettyPackage ofJava(T obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        byte[] content = bos.toByteArray();

        return of(content, (byte) NettySerializeType.JAVA.code());
    }

    public static NettyPackage ofUtf8(String str) throws UnsupportedEncodingException {
        byte[] content = str.getBytes("UTF-8");
        return of(content, (byte) NettySerializeType.UTF8.code());
    }

    public static NettyPackage ofGbk(String str) throws UnsupportedEncodingException {
        byte[] content = str.getBytes("GBK");
        return of(content, (byte) NettySerializeType.GBK.code());
    }

    public static Object parse(NettyPackage pkg) throws IOException, ClassNotFoundException {
        byte serializeType = pkg.serializeType;
        if (serializeType == NettySerializeType.BIN.code()) {
            return parseBin(pkg);
        } else if (serializeType == NettySerializeType.JAVA.code()) {
            return parseJava(pkg);
        } else if (serializeType == NettySerializeType.UTF8.code()) {
            return parseUtf8(pkg);
        } else if (serializeType == NettySerializeType.GBK.code()) {
            return parseGbk(pkg);
        }
        return pkg;
    }

    public static byte[] parseBin(NettyPackage pkg) {
        return pkg.content;
    }

    public static Object parseJava(NettyPackage pkg) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(pkg.content);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object ret = ois.readObject();
        ois.close();
        return ret;
    }

    public static String parseUtf8(NettyPackage pkg) throws UnsupportedEncodingException {
        return new String(pkg.content, "UTF-8");
    }

    public static String parseGbk(NettyPackage pkg) throws UnsupportedEncodingException {
        return new String(pkg.content, "GBK");
    }
}
