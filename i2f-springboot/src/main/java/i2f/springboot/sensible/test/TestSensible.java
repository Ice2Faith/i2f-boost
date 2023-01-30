package i2f.springboot.sensible.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.sensible.handler.AbsDictSensibleHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestSensible extends AbsDictSensibleHandler implements ApplicationRunner {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        TestBean bean = new TestBean();
        bean.setPhone("18200001111");
        bean.setEmail("554711112222@163.com");
        bean.setIdCard("522000190001010001");
        bean.setPassword("123456");
        bean.setRealname("刘明华");
        bean.setCountry(86L);

        String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);

        System.out.println(str);
    }

    @Override
    protected Object decodeDict(String dictType, Object dictValue) {
        String dictCode = dictType;
        if ("country".equals(dictCode)) {
            if (86L == (Long) dictValue) {
                return "中国";
            }
        }
        return "";
    }
}
