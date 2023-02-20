package i2f.tool.crypt.menus.github.jasypt;

import i2f.tool.crypt.IMenuHandler;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author Ice2Faith
 * @date 2023/2/20 16:08
 * @desc
 */
public class JasyptStandardPBEAlgoEncoderMenuHandler implements IMenuHandler {
    public static StandardPBEStringEncryptor encoder = new StandardPBEStringEncryptor();

    @Override
    public String name() {
        return "jasypt-algo-pbe-en";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println(name() + ": require least two argument.");
            System.out.println(name() + " [algorithm] [password] [...string]");
            System.out.println(name() + " PBEWithMD5AndDES hello");
            System.out.println(name() + " PBEWithMD5AndDES hello world");
            return;
        }
        encoder.setAlgorithm(args[0]);
        encoder.setPassword(args[1]);
        for (int i = 2; i < args.length; i++) {
            String item = args[i];
            String encode = encoder.encrypt(item);
            System.out.println(item + "==> (algo=" + args[0] + ", pass=" + args[1] + ") ==> " + encode + " (spring-config) ==> ENC(" + encode + ")");
        }
    }
}
