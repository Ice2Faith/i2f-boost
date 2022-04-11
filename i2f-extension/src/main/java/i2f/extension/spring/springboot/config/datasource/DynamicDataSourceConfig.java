package i2f.extension.spring.springboot.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ltb
 * @date 2022/3/21 10:24
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springboot.config.datasource.enable:true}")
@Configuration
@EnableAutoConfiguration( exclude = {DataSourceAutoConfiguration.class})
public class DynamicDataSourceConfig implements BeanFactoryAware {


    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private DynamicDataSourceProperty dynamicDataSourceProperty;

    /**
     * 功能描述: <br>
     * 〈动态数据源bean 自动配置注册所有数据源〉
     *
     * @param
     * @return javax.sql.DataSource
     * @Author li.he
     * @Date 2020/6/4 16:47
     * @Modifier
     */
    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        log.info("datasource config....");
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        /*获取yml所有数据源配置*/
        Map<String, Map<String,Object>> datasource = dynamicDataSourceProperty.getDatasource();
        Map<Object, Object> dataSourceMap = new HashMap<>(5);
        log.info("datasource finds:"+datasource);
        Optional.ofNullable(datasource).ifPresent(map -> {
            for (Map.Entry<String,  Map<String,Object>> entry : map.entrySet()) {
                log.info("datasource construct...");
                //创建数据源对象
                HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
                String dataSourceId = entry.getKey();
                configDataSource(entry, dataSource);
                log.info("datasource registry:"+dataSourceId);
                /*bean工厂注册每个数据源bean*/
                listableBeanFactory.registerSingleton(dataSourceId, dataSource);
                dataSourceMap.put(dataSourceId, dataSource);
            }
        });
        log.info("DynamicDataSourceConfig DataSource config done.");
        //AbstractRoutingDataSource设置主从数据源
        return new DynamicDataSource(beanFactory.getBean(DataSourceType.MASTER, DataSource.class),          dataSourceMap);
    }

    private void configDataSource(Map.Entry<String , Map<String,Object>> entry, HikariDataSource dataSource) {
        Map<String, Object> dataSourceConfig =  entry.getValue();
        dataSource.setJdbcUrl(String.valueOf(dataSourceConfig.get("url")));
        dataSource.setDriverClassName(String.valueOf(dataSourceConfig.get("driver")));
        dataSource.setUsername(String.valueOf(dataSourceConfig.get("username")));
        dataSource.setPassword(String.valueOf(dataSourceConfig.get("password")));
    }
}
