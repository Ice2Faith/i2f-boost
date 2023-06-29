package i2f.secure;

import i2f.springboot.application.WarBootApplication;
import i2f.springboot.context.EnableSpringContextConfig;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.secure.EnableSecureConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableSpringContextConfig
@EnableCorsConfig
@EnableSecureConfig
@SpringBootApplication(exclude = {

})
public class I2fSecureApplication extends WarBootApplication {

    public static void main(String[] args) {
        startup(I2fSecureApplication.class, args);
    }

}
