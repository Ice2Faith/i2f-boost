package i2f.tool.crypt.menus.github.jasypt;

import i2f.tool.crypt.IMenuHandler;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author Ice2Faith
 * @date 2023/2/20 16:08
 * @desc
 */
public class JasyptBasicTextEncoderMenuHandler implements IMenuHandler {
    public static BasicTextEncryptor encoder = new BasicTextEncryptor();

    @Override
    public String name() {
        return "jasypt-txt-en";
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
            String encode = encoder.encrypt(item);
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode + " (spring-config) ==> ENC(" + encode + ")");
        }
    }
}
