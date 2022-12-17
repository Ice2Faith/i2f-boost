package i2f.tool.crypt.menus.spring.security;

import i2f.tool.crypt.IMenuHandler;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SHA256PasswordEncoderMenuHandler implements IMenuHandler {
    public static PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");

    @Override
    public String name() {
        return "pe-sha-256";
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
            String encode = encoder.encode(item);
            System.out.println(item + " ==> " + encode);
        }
    }
}
