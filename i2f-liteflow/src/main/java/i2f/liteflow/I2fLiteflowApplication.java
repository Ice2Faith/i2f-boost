package i2f.liteflow;

import i2f.springboot.application.WarBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {
		"i2f.liteflow.**.mapper",
		"i2f.liteflow.**.dao"
})
@SpringBootApplication
public class I2fLiteflowApplication extends WarBootApplication {

	public static void main(String[] args) {
		startup(I2fLiteflowApplication.class, args);
	}

}
