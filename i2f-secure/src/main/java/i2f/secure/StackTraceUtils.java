package i2f.secure;

/**
 * @author Ice2Faith
 * @date 2023/6/15 9:07
 * @desc
 */
public class StackTraceUtils {
    public static String getCurrentStackTrace(){
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StringBuilder builder=new StringBuilder();
        for(int i=2;i<trace.length;i++){
            builder.append(trace[i]).append("\n");
        }
        return builder.toString();
    }
}
