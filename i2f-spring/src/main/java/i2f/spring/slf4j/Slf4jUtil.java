package i2f.spring.slf4j;

import i2f.core.trace.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ltb
 * @date 2022/3/26 15:50
 * @desc
 */
public class Slf4jUtil {
    public static Logger getLogger(){
        String className= TraceUtil.getHereInvokerClass();
        Logger logger= LoggerFactory.getLogger(className);
        return logger;
    }
    public static Logger getMethodLogger(){
        String className=TraceUtil.getHereInvokerClass();
        String method=TraceUtil.getHereInvokerMethod();
        Logger logger= LoggerFactory.getLogger(className+"->"+method);
        return logger;
    }
    public static Logger getDebugLogger(){
        StackTraceElement elem=TraceUtil.getHereInvokerTrace();
        String tag= elem.getClassName()+"->"+elem.getMethodName()+" in file:"+elem.getFileName()+" of line:"+elem.getLineNumber();
        Logger logger=LoggerFactory.getLogger(tag);
        return logger;
    }
}
