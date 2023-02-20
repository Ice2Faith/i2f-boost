package i2f.tool.crypt.menus.github.jasypt;

import i2f.tool.crypt.IMenuHandler;
import org.jasypt.encryption.pbe.PBEStringCleanablePasswordEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author Ice2Faith
 * @date 2023/2/20 16:08
 * @desc
 */
public class JasyptStandardPBEDecoderMenuHandler implements IMenuHandler {
    public static PBEStringCleanablePasswordEncryptor encoder = new StandardPBEStringEncryptor();

    @Override
    public String name() {
        return "jasypt-std-pbe-de";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [password] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        encoder.setPassword(args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            if (item.startsWith("ENC(")) {
                item = item.substring("ENC(".length(), item.length() - 1);
            }
            String encode = encoder.decrypt(item);
            System.out.println(item + " ==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
