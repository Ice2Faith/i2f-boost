package i2f.core.generate.core.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.generate.RegexGenerator;
import i2f.core.generate.core.IGenerate;
import i2f.core.lang.functional.common.IMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
public class TrimGenerate  implements IGenerate {
    public IMapper<String, Object> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String prefix; //去除的前缀，可多个，用|进行分隔，例如：and|or
    public String suffix; //去除的后缀，同prefix
    public String sensible; // 是否对大小写敏感，值为：true/false
    public String trimBefore; // 是否在去除前后缀操作之前先trim操作
    public String trimAfter; // 是否在去除前后缀操作之后先trim操作
    public String template; // 指定模板

    @Override
    public String gen() {
        String str= null;
        if(template!=null){
            if("".equals(template)){
                template=null;
            }
        }
        if(template==null){
            str = mapper.get(data);
        }else{
            Map<String,Object> param=new HashMap<>(16);
            param.put("_root",root);
            param.put("_item",data);
            Map<String,Object> ctx=new HashMap<>(16);

            param.put("_ctx",ctx);
            str= RegexGenerator.render(template,param,mapper,basePackages);
        }
        return trim(str);
    }

    private String trim(String str){
        if(str==null){
            return str;
        }
        boolean ignoreCase=false;
        if(sensible!=null){
            if("true".equals(sensible)){
                ignoreCase=false;
            }
            if("false".equals(sensible)){
                ignoreCase=true;
            }
        }
        if(trimBefore!=null){
            if("true".equals(trimBefore)){
                str=str.trim();
            }
        }
        if(prefix!=null){
            String[] prefixes=prefix.split("\\|");
            for(String item : prefixes){
                if("".equals(item)){
                    continue;
                }
                int plen=item.length();
                if(str.length()<plen){
                    continue;
                }
                boolean needTrim=false;
                String sfix=str.substring(0,plen);
                if(ignoreCase){
                    if(item.equalsIgnoreCase(sfix)){
                        needTrim=true;
                    }
                }else{
                    if(item.equals(sfix)){
                        needTrim=true;
                    }
                }
                if(needTrim){
                    str=str.substring(plen);
                    break;
                }
            }
        }
        if(suffix!=null){
            String[] suffixes=suffix.split("\\|");
            for(String item : suffixes){
                if("".equals(item)){
                    continue;
                }
                int plen=item.length();
                if(str.length()<plen){
                    continue;
                }
                boolean needTrim=false;
                String sfix=str.substring(str.length()-plen);
                if(ignoreCase){
                    if(item.equalsIgnoreCase(sfix)){
                        needTrim=true;
                    }
                }else{
                    if(item.equals(sfix)){
                        needTrim=true;
                    }
                }
                if(needTrim){
                    str=str.substring(0,str.length()-plen);
                    break;
                }
            }
        }
        if(trimAfter!=null){
            if("true".equals(trimAfter)){
                str=str.trim();
            }
        }
        return str;
    }
}
