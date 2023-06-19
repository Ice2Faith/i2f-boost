package i2f.tool.crypt.menus.jce;

import i2f.core.jce.encrypt.EncryptUtil;
import i2f.core.jce.encrypt.symmetric.SymmetricEncryptor;
import i2f.tool.crypt.IMenuHandler;

import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class AesDecoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "aes-utf8-de";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least one argument.");
            System.out.println(name() + " [password] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        SymmetricEncryptor encryptor = EncryptUtil.aes(args[0].getBytes("UTF-8"));
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = new String(encryptor.decrypt(Base64.getDecoder().decode(item)), "UTF-8");
            System.out.println(item + "==> (pass=" + args[0] + ") ==> " + encode);
        }
    }
}
