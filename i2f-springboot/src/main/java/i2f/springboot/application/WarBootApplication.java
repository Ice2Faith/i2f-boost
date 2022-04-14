package i2f.springboot.application;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author ltb
 * @date 2022/4/14 18:36
 * @desc 继承 SpringBootServletInitializer 并重写configure方法，使得指向类指向启动类，则可以再war包中启动
 * 在war包中启动，pom.xml需要starter-web排除tomcat
 * 另外打包方式改为war
 */
public class WarBootApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
