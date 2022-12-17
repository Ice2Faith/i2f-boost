package i2f.tool.crypt.menus.base;

import i2f.core.digest.HexStringUtil;
import i2f.core.jce.md.IMessageDigestor;
import i2f.core.jce.md.MessageDigestUtil;
import i2f.core.jce.md.sha.ShaType;
import i2f.tool.crypt.IMenuHandler;

public class HmacSha384MenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "hmac-sha-384";
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
        IMessageDigestor encoder = MessageDigestUtil.hmac(ShaType.SHA384, args[0].getBytes("UTF-8"));
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = HexStringUtil.toHexString(encoder.mds(item.getBytes("UTF-8")));
            System.out.println(item + "==> (key=" + args[0] + ") ==> " + encode);
        }
    }
}
