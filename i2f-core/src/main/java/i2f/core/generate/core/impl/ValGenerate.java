package i2f.core.generate.core.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.generate.core.IGenerate;
import i2f.core.lang.functional.common.IMapper;
import i2f.core.reflection.reflect.core.ReflectResolver;
import lombok.Data;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Author("i2f")
@Data
public class ValGenerate implements IGenerate {
    public IMapper<String, Object> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String userMapper;

    @Override
    public String gen() {
        IMapper<String, Object> currentMapper = getCurrentMapper();
        return currentMapper.get(data);
    }

    private IMapper<String, Object> getCurrentMapper() {
        IMapper<String, Object> ret = this.mapper;
        if (userMapper != null && !"".equals(userMapper)) {
            try {
                Class clazz = ReflectResolver.getClazz(this.userMapper);
                if (clazz != null) {
                    ret = (IMapper<String, Object>) ReflectResolver.instance(clazz);
                }
            } catch (Exception e) {

            }
        }
        return ret;
    }
}
