package i2f.tool.crypt.menus.base;

import i2f.core.digest.HexStringUtil;
import i2f.core.jce.md.IMessageDigestor;
import i2f.core.jce.md.MessageDigestUtil;
import i2f.core.jce.md.sha.ShaType;
import i2f.tool.crypt.IMenuHandler;

public class Sha512MenuHandler implements IMenuHandler {
    private static IMessageDigestor encoder = MessageDigestUtil.sha(ShaType.SHA512);

    @Override
    public String name() {
        return "sha-512";
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
            String encode = HexStringUtil.toHexString(encoder.mds(item.getBytes("UTF-8")));
            System.out.println(item + " ==> " + encode);
        }
    }
}
