package i2f.secure;

import i2f.springboot.application.WarBootApplication;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.secure.EnableSecureConfig;
import i2f.springboot.secure.converter.SecureJacksonMvcConfigurer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@EnableCorsConfig
@EnableSecureConfig
@SpringBootApplication
@Import(SecureJacksonMvcConfigurer.class)
public class I2fSecureApplication extends WarBootApplication {

	public static void main(String[] args) {
		startup(I2fSecureApplication.class, args);
	}

}
