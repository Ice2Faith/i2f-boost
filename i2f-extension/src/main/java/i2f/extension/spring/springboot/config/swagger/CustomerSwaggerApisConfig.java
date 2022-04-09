package i2f.extension.spring.springboot.config.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import i2f.extension.spring.environment.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/9 15:18
 * @desc
 */
@ConditionalOnBean(SwaggerConfig.class)
@Slf4j
@Configuration
@ConditionalOnExpression("${i2f.springboot.config.swagger.apis-enable:true}")
public class CustomerSwaggerApisConfig implements InitializingBean, ApplicationContextAware {

    public static final String SWAGGER_API_PREFIX="i2f.springboot.config.swagger.apis.";

    @Autowired
    private ApiInfo apiInfo;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Autowired
    private Environment environment;

    private Map<String,Map<String, String>> customers=new HashMap<>();

    private List<String> customersList=new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Map<String,Object>> apis= EnvironmentUtil.getGroupMapConfigs(environment,SWAGGER_API_PREFIX);

        for(Map.Entry<String,Map<String, Object>> entry : apis.entrySet()){
            String name=entry.getKey();
            Map<String, Object> item=entry.getValue();
            if(applicationContext.containsBean(name)){
                log.warn("context has contains bean with name:"+name);
                continue;
            }

            String groupName=(String)item.get("group");
            String basePackage=(String)item.get("base-package");
            String antPath=(String)item.get("ant-path");

            if(groupName==null || "".equals(groupName)){
                log.warn("swagger registry docket bad group.");
                continue;
            }

            if(basePackage==null || "".equals(basePackage)){
                log.warn("swagger registrt docket bad base-package.");
                continue;
            }

            if(antPath==null || "".equals(antPath)){
                log.warn("swagger registry bad ant-path be replace to /**");
                antPath="/**";
            }

            Map<String,String> cus=new HashMap<>();
            cus.put("group",groupName);
            cus.put("pkg",basePackage);
            cus.put("path",antPath);
            customers.put(name,cus);
            customersList.add(name);

            if(true){
                continue;
            }

            //获取BeanFactory
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            //创建bean信息.
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(DocketFactoryBean.class);
            beanDefinitionBuilder.addPropertyValue("apiInfo",apiInfo)
                    .addPropertyValue("groupName",groupName)
                    .addPropertyValue("basePackage",basePackage)
                    .addPropertyValue("antPath",antPath);
            //动态注册bean.
            defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
            log.info("swagger registry docket name is:"+name);

            Docket dock=(Docket)applicationContext.getBean(name);
            log.info("swagger dock is:"+dock);
        }
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=1")
    @Bean
    public Docket customerOneApi() {
        return customerConfigProxy(0);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=2")
    @Bean
    public Docket customerTwoApi() {
        return customerConfigProxy(1);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=3")
    @Bean
    public Docket customerThreeApi() {
        return customerConfigProxy(2);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=4")
    @Bean
    public Docket customerFourApi() {
        return customerConfigProxy(3);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=5")
    @Bean
    public Docket customerFiveApi() {
        return customerConfigProxy(4);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=6")
    @Bean
    public Docket customerSixApi() {
        return customerConfigProxy(5);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=7")
    @Bean
    public Docket customerSevenApi() {
        return customerConfigProxy(6);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=8")
    @Bean
    public Docket customerEightApi() {
        return customerConfigProxy(7);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.apis-count:0}>=9")
    @Bean
    public Docket customerNineApi() {
        return customerConfigProxy(8);
    }

    private Docket customerConfigProxy(int idx){
        Map<String,String> conf=customers.get(customersList.get(idx));
        String groupName=(idx+1)+"-"+conf.get("group");
        log.info("SwaggerConfig customer "+idx+" api config:"+groupName);
        String pkgs=conf.get("pkg");
        String paths=conf.get("path");
        List<Predicate<RequestHandler>> pkgList=new ArrayList<>();
        for(String pkg : pkgs.split(",")){
            pkgList.add(RequestHandlerSelectors
                    .basePackage(pkg));
        }
        Predicate<RequestHandler> apis= Predicates.or(pkgList);

        List<Predicate<String>> pathList=new ArrayList<>();
        for(String path : paths.split(",")){
            pathList.add(PathSelectors.ant(path));
        }

        Predicate<String> selectPaths=Predicates.or(pathList);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .select()
                .apis(apis)
                .paths(selectPaths)
                .build()
                .apiInfo(apiInfo);
    }
}
