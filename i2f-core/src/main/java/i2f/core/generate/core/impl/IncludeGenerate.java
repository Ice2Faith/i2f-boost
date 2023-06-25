package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.generate.RegexGenerator;
import i2f.core.generate.core.IGenerate;
import i2f.core.lang.functional.common.IMapper;
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
public class IncludeGenerate implements IGenerate {
    public IMapper<String, Object> mapper;
    public Object root;
    public Object data;
    public String template;
    public List<String> basePackages;

    @Override
    public String gen() {
        if(data==null){
            return mapper.get(data);
        }
        if(template!=null){
            String tpl=template.trim();
            if("".equals(tpl)){
                template=null;
            }
        }
        int slen=template==null?64:Math.max(template.length(),64);

        StringBuilder builder=new StringBuilder(slen);
        if(template!=null){
            Map<String,Object> param=new HashMap<>(16);
            param.put("_item",data);
            param.put("_root",root);
            Map<String,Object> ctx=new HashMap<>(16);

            param.put("_ctx",ctx);
            String str= RegexGenerator.render(template,param,mapper,basePackages);
            builder.append(str);
        }else{
            String str = mapper.get(data);
            builder.append(str);
        }
        return builder.toString();
    }
}
