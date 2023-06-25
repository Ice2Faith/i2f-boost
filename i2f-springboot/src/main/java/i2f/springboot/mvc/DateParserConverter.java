package i2f.springboot.mvc;

import i2f.core.type.date.Dates;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class DateParserConverter implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        return Dates.from(s);
    }
}
