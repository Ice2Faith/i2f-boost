package i2f.core.os.command;

import java.io.File;
import java.net.URL;

/**
 * @author ltb
 * @date 2022/7/8 11:28
 * @desc
 */
public class WindowsCmd {
    public static void open(String str){
        try{
            CmdLineExecutor.run("explorer \""+str+"\"");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void open(File file){
        open(file.getAbsolutePath());
    }
    public static void open(URL url){
        open(url.toString());
    }

}
