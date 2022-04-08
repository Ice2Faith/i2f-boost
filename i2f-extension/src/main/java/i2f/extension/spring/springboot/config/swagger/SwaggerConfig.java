package i2f.extension.spring.springboot.config.swagger;

import i2f.extension.spring.environment.EnvironmentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;

@ConditionalOnExpression("${i2f.springboot.config.swagger.enable:true}")
@Slf4j
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "i2f.springboot.config.swagger")
public class SwaggerConfig  implements InitializingBean, ApplicationContextAware {

    public static final String SWAGGER_API_PREFIX="i2f.springboot.config.swagger.apis.";

    private String title="Micro-Service Project Api";
    private String description="Micro-Service project.";
    private String license="Apache 2.0";
    private String licenseUrl="http://www.apache.org/licenses/LICENSE-2.0.html";
    private String version="1.0.0";

    @Bean
    public ApiInfo apiEndPointsInfo() {
        log.info("SwaggerConfig api info config.");
        return new ApiInfoBuilder().title(title)
                .description(description)
                .contact(new Contact(null, null, null))
                .license(license)
                .licenseUrl(licenseUrl)
                .version(version)
                .build();
    }

    @Bean
    public Docket allApi() {
        log.info("SwaggerConfig all api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("all")
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Autowired
    private Environment environment;

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

            //获取BeanFactory
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            //创建bean信息.
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(DocketFactoryBean.class);
            beanDefinitionBuilder.addPropertyValue("apiInfo",apiEndPointsInfo())
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
}
