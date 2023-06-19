package i2f.tool.crypt.menus.base;

import i2f.core.jce.codec.CodecUtil;
import i2f.core.jce.digest.IMessageDigester;
import i2f.core.jce.digest.MessageDigestUtil;
import i2f.core.jce.digest.md.MdType;
import i2f.tool.crypt.IMenuHandler;

public class HmacMd5MenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "hmac-md5";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [key] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        IMessageDigester encoder = MessageDigestUtil.hmac(MdType.MD5, args[0].getBytes("UTF-8"));
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = CodecUtil.toHexString(encoder.mds(item.getBytes("UTF-8")));
            System.out.println(item + "==> (key=" + args[0] + ") ==> " + encode);
        }
    }
}
