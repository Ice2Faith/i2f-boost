package i2f.extension.spring.springboot.config.mvc;

import i2f.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class DateParserConverter implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        return DateUtil.from(s);
    }
}
