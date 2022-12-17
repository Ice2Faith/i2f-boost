package i2f.tool.crypt.menus.base;

import i2f.tool.crypt.IMenuHandler;

import java.util.Base64;

public class Base64EncoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "base64-en";
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
            String encode = Base64.getEncoder().encodeToString(item.getBytes("UTF-8"));
            System.out.println(item + " ==> " + encode);
        }
    }
}
