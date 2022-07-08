package i2f.core.zplugin.log.context;

import i2f.core.properties.PropertiesUtil;
import i2f.core.zplugin.log.controls.LogDecision;
import i2f.core.zplugin.log.controls.impl.PackageClassMethodLogLevelDecision;
import i2f.core.zplugin.log.enums.LogLevel;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/4/1 16:36
 * @desc
 */
public class LogLevelMappingHolder {
    public static final String LOG_LEVEL_MAPPING_PREFIX="log.level.";
    private static volatile LogDecision decision=new PackageClassMethodLogLevelDecision();
    private static volatile ConcurrentHashMap<String, LogLevel> mapping=new ConcurrentHashMap<>();
    public static void setDecision(LogDecision decision){
        LogLevelMappingHolder.decision=decision;
    }
    public static LogDecision getDecision(){
        return LogLevelMappingHolder.decision;
    }
    public static ConcurrentHashMap<String,LogLevel> getMapping(){
        return LogLevelMappingHolder.mapping;
    }
    public static LogLevel level(String classNamePatten){
        return LogLevelMappingHolder.mapping.get(classNamePatten);
    }
    public static void addMapping(String classNamePatten,LogLevel level){
        LogLevelMappingHolder.mapping.put(classNamePatten,level);
    }
    public static void removeMapping(String classNamePatten){
        LogLevelMappingHolder.mapping.remove(classNamePatten);
    }

    /**
     * 对map中的每一个以prefix开头的patten对应的value添加写出器
     * 例如：
     * prefix=log.level.
     * 有数据：
     * log.level.com.i2f.*.mapper=DEBUG
     * 则注册一个匹配为com.i2f.*.mapper的包位置，日志控制为debug级别
     * @param map
     * @param prefix
     */
    public static void addMappings(Map<String,Object> map,String prefix){
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
            if(val instanceof LogLevel){
                addMapping(name,(LogLevel)val);
            }else{
                String lvl=String.valueOf(val);
                addMapping(name,LogLevel.parse(lvl));
            }
        }
    }
    public static void addMappings(Properties prop,String prefix){
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
            if(val instanceof LogLevel){
                addMapping(name,(LogLevel)val);
            }else{
                String lvl=String.valueOf(val);
                addMapping(name,LogLevel.parse(lvl));
            }
        }
    }

    public static void addMappingsByClasspathProperties(String fileName) throws IOException {
        Properties prop=PropertiesUtil.loadResource(fileName);
        addMappings(prop,LOG_LEVEL_MAPPING_PREFIX);
    }

}
