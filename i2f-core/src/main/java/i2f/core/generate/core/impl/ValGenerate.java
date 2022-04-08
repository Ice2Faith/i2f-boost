package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.generate.core.IGenerate;
import i2f.core.interfaces.IMap;
import i2f.core.reflect.core.ReflectResolver;
import lombok.Data;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
@Data
public class ValGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String userMapper;

    @Override
    public String gen() {
        IMap<Object,String> currentMapper=getCurrentMapper();
        return currentMapper.map(data);
    }

    private IMap<Object,String> getCurrentMapper(){
        IMap<Object,String> ret=this.mapper;
        if(userMapper!=null && !"".equals(userMapper)){
            try{
                Class clazz= ReflectResolver.getClazz(this.userMapper);
                if(clazz!=null){
                    ret=(IMap<Object, String>) ReflectResolver.instance(clazz);
                }
            }catch(Exception e){

            }
        }
        return ret;
    }
}
