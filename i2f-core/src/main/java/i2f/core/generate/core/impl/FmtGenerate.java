package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IMapper;
import i2f.core.generate.core.IGenerate;
import i2f.core.generate.core.ObjectFinder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
@Data
public class FmtGenerate implements IGenerate {
    public IMapper<String, Object> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String format;
    public String values;

    @Override
    public String gen() {
        if(data==null){
            return mapper.get(data);
        }
        Object[] formatArgs=getFormatValues();

        return String.format(format,formatArgs);
    }

    private Object[] getFormatValues(){
        if(values==null || "".equals(values)){
            return new Object[]{data};
        }

        String[] keys=values.split(",");
        Object[] vals=new Object[keys.length];

        Map<String,Object> param=new HashMap<>(16);
        param.put("_root",root);
        param.put("_item",data);
        Map<String,Object> ctx=new HashMap<>(16);

        param.put("_ctx",ctx);

        for (int i = 0; i < keys.length; i++) {
            String key=keys[i];
            key=key.trim();
            Object val= ObjectFinder.getObjectByDotKeyWithReference(param,key);
            vals[i]=val;
        }

        return vals;
    }


}
