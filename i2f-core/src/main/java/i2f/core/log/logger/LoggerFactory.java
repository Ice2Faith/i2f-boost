package i2f.core.log.logger;

import i2f.core.check.CheckUtil;
import i2f.core.log.annotations.Log;
import i2f.core.log.core.LogCore;

/**
 * @author Ice2Faith
 * @date 2023/8/2 11:11
 * @desc
 */
public class LoggerFactory {
    public static Logger getLogger(String location){
        return new LoggerImpl(location);
    }
    public static Logger getLogger(Class<?> clazz){
        Log ann = LogCore.getAnnotation(clazz, Log.class);
        if(ann!=null){
            if(!CheckUtil.isEmptyStr(ann.module())){
                return new LoggerImpl(clazz.getName(),ann.module());
            }
        }
        return new LoggerImpl(clazz.getName());
    }

    public static Logger getLogger(String location,String module){
        return new LoggerImpl(location,module);
    }
    public static Logger getLogger(Class<?> clazz,String module){
        if(CheckUtil.isEmptyStr(module)){
            return getLogger(clazz);
        }
        return getLogger(clazz.getName(),module);
    }
}
