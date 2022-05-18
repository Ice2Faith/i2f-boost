package test.model;

import i2f.core.reflect.bean.BeanTagResolver;
import i2f.core.reflect.bean.annotations.BeanTag;
import i2f.core.zplugin.validate.impl.VNotEmpty;
import i2f.core.zplugin.validate.impl.VNotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/3/25 11:23
 * @desc
 */
@Data
@NoArgsConstructor
public class TestBean {

    @VNotEmpty
    @BeanTag("public")
    private String account;

    @BeanTag("private")
    private String password;

    @VNotNull
    @BeanTag("private")
    private String tel;

    @BeanTag("public")
    private String name;

    @BeanTag("public")
    private String sex;

    @BeanTag("private")
    private int age;

    public static void main(String[] args){
        TestBean srcBean=new TestBean();
        srcBean.setAccount("admin");
        srcBean.setPassword("123456");
        srcBean.setTel("18066664444");
        srcBean.setName("Zhang");
        srcBean.setSex("man");
        srcBean.setAge(22);

        TestBean dstBean=new TestBean();
        BeanTagResolver.copyByTag(srcBean,dstBean,"public");

        System.out.println(srcBean);
        System.out.println(dstBean);

    }
}
