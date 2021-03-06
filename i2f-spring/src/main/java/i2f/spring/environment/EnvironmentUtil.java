package i2f.spring.environment;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 16:57
 * @desc
 */
@Component
public class EnvironmentUtil implements EnvironmentAware {

    protected Environment env;

    public EnvironmentUtil(Environment env) {
        this.env = env;
    }

    public Environment getEnv() {
        return env;
    }

    public EnvironmentUtil setEnv(Environment env) {
        this.env = env;
        return this;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }

    ///////////////////////////////////////
    public static EnvironmentUtil of(Environment env){
        return new EnvironmentUtil(env);
    }


    public String get(String name){
        return env.getProperty(name);
    }

    public int getInt(String name,int def){
        String str=env.getProperty(name);
        if(str==null){
            return def;
        }
        str=str.trim();
        try{
            return Integer.parseInt(str);
        }catch(Exception e){

        }
        return def;
    }

    public double getDouble(String name,double def){
        String str=env.getProperty(name);
        if(str==null){
            return def;
        }
        str=str.trim();
        try{
            return Double.parseDouble(str);
        }catch(Exception e){

        }
        return def;
    }

    public long getLong(String name,long def){
        String str=env.getProperty(name);
        if(str==null){
            return def;
        }
        str=str.trim();
        try{
            return Long.parseLong(str);
        }catch(Exception e){

        }
        return def;
    }

    public float getFloat(String name,float def){
        String str=env.getProperty(name);
        if(str==null){
            return def;
        }
        str=str.trim();
        try{
            return Float.parseFloat(str);
        }catch(Exception e){

        }
        return def;
    }

    public boolean getBoolean(String name,boolean def){
        String str=env.getProperty(name);
        if(str==null){
            return def;
        }
        str=str.trim().toLowerCase();
        try{
            return Boolean.parseBoolean(str);
        }catch(Exception e){

        }
        return def;
    }

    public String[] getArray(String name,String splitReg,int limit){
        String str=env.getProperty(name);
        if(str==null){
            return new String[0];
        }
        str=str.trim();
        return str.split(splitReg,limit);
    }

    public String[] getArray(String name,String splitReg){
        return getArray(name,splitReg,0);
    }

    /**
     * ???????????????????????????????????????????????????
     * key?????????????????????
     * value????????????????????????????????????map
     * @return
     */
    public Map<String, Map<String, Object>> getEnvironmentProperties() {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) env;
        Iterator<PropertySource<?>> iterator = configurableEnvironment.getPropertySources().iterator();

        while (iterator.hasNext()) {
            Map<String, Object> props = new HashMap<>(128);
            PropertySource<?> source = iterator.next();
            String name = source.getName();
            Object src = source.getSource();
            if (src instanceof Map) {
                for (Map.Entry<String, Object> entry : ((Map<String, Object>) src).entrySet()) {
                    try{
                        String key = entry.getKey();
                        props.put(key, configurableEnvironment.getProperty(key));
                    }catch(Exception e){

                    }
                }
            }
            map.put(name, props);
        }
        return map;
    }

    /**
     * ???????????????????????????????????????????????????
     * @param keepPrefix ??????????????????
     * @param prefix ??????
     * @return
     */
    public Map<String, Object> getPropertiesWithPrefix( boolean keepPrefix, String prefix) {
        return getPropertiesWithPrefix(getEnvironmentProperties(), keepPrefix, prefix);
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param envProperties ????????????
     * @param keepPrefix ??????????????????
     * @param prefix ??????
     * @return
     */
    public Map<String, Object> getPropertiesWithPrefix(Map<String, Map<String, Object>> envProperties, boolean keepPrefix, String prefix) {
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
     * ?????????????????????groupPrefix???????????????????????????????????????????????????????????????map,
     * ???????????????id(string):????????????(map)????????????
     * ???????????????
     * ?????????
     * spring.datasource.master.url=
     * spring.datasource.master.driver=
     *
     * spring.datasource.slave.url=
     * spring.datasource.slave.driver=
     * ??????????????????????????????????????????????????????groupPrefix=spring.datasource.
     * ????????????????????????
     * master:
     *  url:
     *  driver:
     * slave:
     *  url:
     *  driver:
     * ?????????master???slave?????????ID?????????????????????key,
     * ??????????????????????????????url???driver????????????value
     * @param groupPrefix
     * @return
     */
    public Map<String, Map<String,Object>> getGroupMapConfigs(String groupPrefix){
        Map<String, Map<String,Object>> ret=new HashMap<>();
        Map<String, Map<String, Object>> map = getEnvironmentProperties();
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
