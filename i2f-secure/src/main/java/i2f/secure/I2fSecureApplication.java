package i2f.secure;

import i2f.springboot.application.WarBootApplication;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.secure.EnableSecureConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCorsConfig
@EnableSecureConfig
@SpringBootApplication
public class I2fSecureApplication extends WarBootApplication {

	public static void main(String[] args) {
		startup(I2fSecureApplication.class, args);
	}

}
