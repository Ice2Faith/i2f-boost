package i2f.core.zplugin.log.context;

import i2f.core.properties.PropertiesUtil;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.zplugin.log.ILogWriter;
import i2f.core.zplugin.log.impl.BroadcastLogWriter;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author ltb
 * @date 2022/3/30 9:11
 * @desc
 */
public class LogWriterHolder {
    public static final String LOG_WRITER_TYPE_PREFIX="log.writer.";

    private static volatile BroadcastLogWriter writer=new BroadcastLogWriter();
    public static ILogWriter getWriter(){
        return LogWriterHolder.writer;
    }
    public static void registerLogWriter(Class<? extends ILogWriter> clazz){
        LogWriterHolder.writer.registerLogWriter(clazz);
    }
    public static void registerLogWriter(String name,Class<? extends ILogWriter> clazz){
        LogWriterHolder.writer.registerLogWriter(name,clazz);
    }
    public static void registerLogWriter(String name,ILogWriter writer){
        LogWriterHolder.writer.registerLogWriter(name,writer);
    }
    public static ILogWriter removeLogWriter(String name){
        return LogWriterHolder.writer.removeLogWriter(name);
    }

    /**
     * 对map中的每一个以prefix开头的name对应的value添加写出器
     * 例如：
     * prefix=log.writer.
     * 有数据：
     * log.writer.file=com.i2f.log.impl.ContextFileLogWriter
     * 则注册一个名为file的写出，实现类为=后的类
     * @param map
     * @param prefix
     */
    public static void registerLogWriters(Map<String,Object> map, String prefix){
        for(String item : map.keySet()){
            String name=item;
            if(prefix!=null){
                if(name.startsWith(prefix)){
                    name=name.substring(prefix.length());
                }
            }
            if("".equals(name)){
                continue;
            }
            Object val=map.get(item);
            if(val instanceof ILogWriter){
                registerLogWriter(name,(ILogWriter)val);
            }else{
                String lvl=String.valueOf(val);
                Class<ILogWriter> clazz= ReflectResolver.getClazz(lvl);
                if(clazz!=null) {
                    registerLogWriter(name, clazz);
                }
            }
        }
    }
    public static void registerLogWriters(Properties prop, String prefix){
        for(Object item : prop.keySet()){
            String name=String.valueOf(item);
            if(prefix!=null){
                if(name.startsWith(prefix)){
                    name=name.substring(prefix.length());
                }
            }
            if("".equals(name)){
                continue;
            }
            Object val=prop.get(item);
            if(val instanceof ILogWriter){
                registerLogWriter(name,(ILogWriter)val);
            }else{
                String lvl=String.valueOf(val);
                Class<ILogWriter> clazz= ReflectResolver.getClazz(lvl);
                if(clazz!=null) {
                    registerLogWriter(name, clazz);
                }
            }
        }
    }

    public static void registerLogWritersByClasspathProperties(String fileName) throws IOException {
        Properties prop= PropertiesUtil.loadResource(fileName);
        registerLogWriters(prop,LOG_WRITER_TYPE_PREFIX);
    }
}
