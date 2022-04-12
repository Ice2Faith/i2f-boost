package i2f.springboot.datasource;

import i2f.spring.environment.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/21 11:00
 * @desc
 */
@Slf4j
@ConditionalOnExpression("${i2f.springboot.config.datasource.enable:true}")
@Configuration
public class DynamicDataSourceProperty implements InitializingBean {

    public static final String MULTIPLY_DATASOURCE_PREFIX="i2f.springboot.config.datasource.multiply.";
    @Autowired
    private Environment environment;

    private Map<String, Map<String,Object>> properties=new HashMap<>();


    public static Map<String,Map<String,Object>> getDatasourceConfigs(Environment env){
        return EnvironmentUtil.getGroupMapConfigs(env,MULTIPLY_DATASOURCE_PREFIX);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
       properties = getDatasourceConfigs(environment);
       for(Map.Entry<String,Map<String,Object>> item : properties.entrySet()){
           log.info("multiply datasource find: "+item.getKey());
           log.info("datasource is:"+item.getValue());
       }

       if(!properties.containsKey(DataSourceType.MASTER)){
           resolveDefaultSpringDatasourceAsMaster(properties);
       }

        if(!properties.containsKey(DataSourceType.MASTER)){
            throw new BeanInitializationException("datasource ["+DataSourceType.MASTER+"] not found config.please config properties like as:\n" +
                    "\t"+MULTIPLY_DATASOURCE_PREFIX+"master.url=jdbc:xxx\n" +
                    "\t"+MULTIPLY_DATASOURCE_PREFIX+"master.username=xxx\n" +
                    "\t"+MULTIPLY_DATASOURCE_PREFIX+"master.password=xxx\n" +
                    "\t"+MULTIPLY_DATASOURCE_PREFIX+"master.driver=xxx\n" +
                    "\t\tcomment: .master is required other datasource is option,format like:\n" +
                    "\t\t"+MULTIPLY_DATASOURCE_PREFIX+"${datasourceId}.[url|username|password|driver]");
        }
    }

    public Map<String, Map<String,Object>> getDatasource() {
        return properties;
    }

    private void resolveDefaultSpringDatasourceAsMaster(Map<String, Map<String,Object>> properties){
        Map<String,Object> map=EnvironmentUtil.getPropertiesWithPrefix(environment,false,"spring.datasource.");
        Map<String,Object> res=new HashMap<>();
        if(!map.containsKey("url")){
            return;
        }
        res.put("url",map.get("url"));
        res.put("username",map.get("username"));
        res.put("password",map.get("password"));
        res.put("driver",map.get("driver-class-name"));
        properties.put(DataSourceType.MASTER,res);
    }
}
