package i2f.tool.crypt.menus;

import i2f.core.resource.ResourceUtil;
import i2f.tool.crypt.IMenuHandler;

public class HelpMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(String[] args) throws Exception {
        String help = ResourceUtil.getClasspathResourceAsString("static/help.txt", "UTF-8");
        System.out.println(help);
    }
}
