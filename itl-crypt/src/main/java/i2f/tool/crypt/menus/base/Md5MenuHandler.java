package i2f.tool.crypt.menus.base;

import i2f.core.jce.codec.CodecUtil;
import i2f.core.jce.digest.IMessageDigester;
import i2f.core.jce.digest.MessageDigestUtil;
import i2f.core.jce.digest.md.MdType;
import i2f.tool.crypt.IMenuHandler;

public class Md5MenuHandler implements IMenuHandler {
    private static IMessageDigester encoder = MessageDigestUtil.md(MdType.MD5);

    @Override
    public String name() {
        return "md5";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }

        for (String item : args) {
            String encode = CodecUtil.toHexString(encoder.mds(item.getBytes("UTF-8")));
            System.out.println(item + " ==> " + encode);
        }
    }
}
