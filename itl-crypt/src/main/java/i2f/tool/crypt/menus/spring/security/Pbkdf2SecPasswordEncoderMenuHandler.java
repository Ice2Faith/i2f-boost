package i2f.tool.crypt.menus.spring.security;

import i2f.tool.crypt.IMenuHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class Pbkdf2SecPasswordEncoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "pe-pbkdf2-sec";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least two argument.");
            System.out.println(name() + " [secret] [...string]");
            System.out.println(name() + " key hello");
            System.out.println(name() + " key hello world");
            return;
        }
        PasswordEncoder encoder = new Pbkdf2PasswordEncoder(args[0]);
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            String encode = encoder.encode(item);
            System.out.println(item + "==> (srcret=" + args[0] + ") ==> " + encode);
        }
    }
}
