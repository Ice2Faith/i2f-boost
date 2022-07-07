package i2f.generator.api;

import lombok.Data;

import java.lang.reflect.Type;

/**
 * @author ltb
 * @date 2022/7/6 8:48
 * @desc
 */
@Data
public class ApiLine {
    private String parent;
    private String name;
    private String restrict;
    private Type type;
    private String width;
    private String comment;
    private String remark;
    private String order;
    private String typeName;
    private String route;

    public ApiLine refresh(boolean keepJavaLang,boolean keepAll){
        typeName=ApiMethodResolver.getTypeName(type,keepJavaLang,keepAll);
        return this;
    }
}
