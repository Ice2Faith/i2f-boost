package i2f.extension.spring.environment;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 16:57
 * @desc
 */
public class EnvironmentUtil {
    /**
     * 将环境处理为一个方便理解的数据结构
     * key表示每一个环境
     * value表示每一个环境中的配置的map
     * @param env
     * @return
     */
    public static Map<String, Map<String, Object>> getEnvironmentProperties(Environment env) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        StandardEnvironment standardServletEnvironment = (StandardEnvironment) env;
        Iterator<PropertySource<?>> iterator = standardServletEnvironment.getPropertySources().iterator();

        while (iterator.hasNext()) {
            Map<String, Object> props = new HashMap<>(128);
            PropertySource<?> source = iterator.next();
            String name = source.getName();
            Object src = source.getSource();
            if (src instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) src).entrySet()) {
                    String key = entry.getKey();
                    props.put(key, standardServletEnvironment.getProperty(key));
                }
            }
            map.put(name, props);
        }
        return map;
    }

    /**
     * 直接从环境中获取指定前缀的所有配置
     * @param env 环境
     * @param keepPrefix 是否保留前缀
     * @param prefix 前缀
     * @return
     */
    public static Map<String, Object> getPropertiesWithPrefix(Environment env, boolean keepPrefix, String prefix) {
        return getPropertiesWithPrefix(getEnvironmentProperties(env), keepPrefix, prefix);
    }

    /**
     * 从配置对象中获取某些指定前缀的配置信息
     * @param envProperties 配置对象
     * @param keepPrefix 是否保留前缀
     * @param prefix 前缀
     * @return
     */
    public static Map<String, Object> getPropertiesWithPrefix(Map<String, Map<String, Object>> envProperties, boolean keepPrefix, String prefix) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Map<String, Object>> map = envProperties;
        for (Map.Entry<String, Map<String, Object>> item : map.entrySet()) {
            Map<String, Object> entire = item.getValue();
            for (Map.Entry<String, Object> cfg : entire.entrySet()) {
                String key = cfg.getKey();
                if (!key.startsWith(prefix)) {
                    continue;
                }
                Object val = cfg.getValue();

                if (!keepPrefix) {
                    key = key.substring(prefix.length());
                }
                ret.put(key, val);
            }
        }
        return ret;
    }

    /**
     * 从环境中获取以groupPrefix开头的每一个分组，分别将分组信息包装为一个map,
     * 返回以分组id(string):分组对象(map)形式返回
     * 分组概念：
     * 例如：
     * spring.datasource.master.url=
     * spring.datasource.master.driver=
     *
     * spring.datasource.slave.url=
     * spring.datasource.slave.driver=
     * 这样的配置数据，则可以得到分组的前缀groupPrefix=spring.datasource.
     * 则取出分组数据：
     * master:
     *  url:
     *  driver:
     * slave:
     *  url:
     *  driver:
     * 其中的master和slave被称为ID，作为返回值的key,
     * 每一组数据中的数据被url和driver被包装为value
     * @param env
     * @param groupPrefix
     * @return
     */
    public static Map<String, Map<String,Object>> getGroupMapConfigs(Environment env,String groupPrefix){
        Map<String, Map<String,Object>> ret=new HashMap<>();
        Map<String, Map<String, Object>> map = EnvironmentUtil.getEnvironmentProperties(env);
        for(Map.Entry<String,Map<String, Object>> item : map.entrySet()){
            Map<String, Object> entire=item.getValue();
            for(Map.Entry<String,Object> cfg : entire.entrySet()){
                String key=cfg.getKey();
                if(!key.startsWith(groupPrefix)){
                    continue;
                }
                String type=key.substring(groupPrefix.length());
                int idx=type.indexOf(".");
                String typeKey=type.substring(0,idx);
                if(!ret.containsKey(typeKey)){
                    ret.put(typeKey, new HashMap<>());
                }
                String propKey=type.substring(idx+1);
                ret.get(typeKey).put(propKey,cfg.getValue());
            }
        }
        return ret;
    }

}
