package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.generate.core.IGenerate;
import i2f.core.interfaces.IMap;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
@Data
public class DatefmtGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String format;

    @Override
    public String gen() {
        if(data==null){
            return mapper.map(data);
        }
        if(format==null){
            return mapper.map(data);
        }
        if(data instanceof Date){
            SimpleDateFormat fmt=new SimpleDateFormat(format);
            Date date=(Date)data;
            return fmt.format(date);
        }
        return mapper.map(data);
    }


}
