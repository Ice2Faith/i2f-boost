package i2f.tool.crypt;

import i2f.tool.crypt.menus.HelpMenuHandler;
import i2f.tool.crypt.menus.base.*;
import i2f.tool.crypt.menus.spring.security.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CryptMain {
    public static final IMenuHandler helpHandler = new HelpMenuHandler();
    public static Map<String, IMenuHandler> menuHandlerMap = new ConcurrentHashMap<>();

    public static void addMenuHandler(IMenuHandler handler) {
        if (handler == null) {
            return;
        }
        menuHandlerMap.put(handler.name(), handler);
    }

    static {
        addMenuHandler(helpHandler);
        addMenuHandler(new Base64EncoderMenuHandler());
        addMenuHandler(new Base64DecoderMenuHandler());
        addMenuHandler(new Base64UrlEncoderMenuHandler());
        addMenuHandler(new Base64UrlDecoderMenuHandler());
        addMenuHandler(new UrlEncoderMenuHandler());
        addMenuHandler(new UrlDecoderMenuHandler());
        addMenuHandler(new Md2MenuHandler());
        addMenuHandler(new Md5MenuHandler());
        addMenuHandler(new Sha1MenuHandler());
        addMenuHandler(new Sha224MenuHandler());
        addMenuHandler(new Sha256MenuHandler());
        addMenuHandler(new Sha384MenuHandler());
        addMenuHandler(new Sha512MenuHandler());
        addMenuHandler(new HmacMd2MenuHandler());
        addMenuHandler(new HmacMd5MenuHandler());
        addMenuHandler(new HmacSha1MenuHandler());
        addMenuHandler(new HmacSha224MenuHandler());
        addMenuHandler(new HmacSha256MenuHandler());
        addMenuHandler(new HmacSha384MenuHandler());
        addMenuHandler(new HmacSha512MenuHandler());

        addMenuHandler(new Argon2PasswordEncoderMenuHandler());
        addMenuHandler(new BCryptPasswordEncoderMenuHandler());
        addMenuHandler(new LdapShaPasswordEncoderMenuHandler());
        addMenuHandler(new MD2PasswordEncoderMenuHandler());
        addMenuHandler(new MD4PasswordEncoderMenuHandler());
        addMenuHandler(new MD5PasswordEncoderMenuHandler());
        addMenuHandler(new NoOpPasswordEncoderMenuHandler());
        addMenuHandler(new Pbkdf2PasswordEncoderMenuHandler());
        addMenuHandler(new Pbkdf2SecPasswordEncoderMenuHandler());
        addMenuHandler(new SCryptPasswordEncoderMenuHandler());
        addMenuHandler(new SHA1PasswordEncoderMenuHandler());
        addMenuHandler(new SHA224PasswordEncoderMenuHandler());
        addMenuHandler(new SHA256PasswordEncoderMenuHandler());
        addMenuHandler(new SHA384PasswordEncoderMenuHandler());
        addMenuHandler(new SHA512PasswordEncoderMenuHandler());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            args = new String[]{helpHandler.name()};
        }

        String option = args[0].toLowerCase();

        try {
            IMenuHandler handler = menuHandlerMap.get(option);
            if (handler == null) {
                handler = helpHandler;
            }
            String[] handlerArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                handlerArgs[i - 1] = args[i];
            }
            handler.execute(handlerArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
