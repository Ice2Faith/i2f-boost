package i2f.extension.spring.springboot.config.swagger;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ltb
 * @date 2022/4/8 0:13
 * @desc
 */
@Data
public class DocketFactoryBean implements FactoryBean {

    private ApiInfo apiInfo;

    private String groupName;

    private String basePackage;

    private String antPath;

    @Override
    public Object getObject() throws Exception {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage(basePackage))
                .paths(PathSelectors.ant(antPath))
                .build()
                .apiInfo(apiInfo);
    }

    @Override
    public Class<?> getObjectType() {
        return Docket.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
