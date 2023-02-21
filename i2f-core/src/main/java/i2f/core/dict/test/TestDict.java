package i2f.core.dict.test;


import i2f.core.dict.resolver.DictResolver;

/**
 * @author Ice2Faith
 * @date 2023/2/21 13:45
 * @desc
 */
public class TestDict {
    public static void main(String[] args) throws Exception {
        TestBean bean = new TestBean();
        bean.setSex(1);
        bean.setGrade("1");

        DictResolver.decode(bean);

        System.out.println(bean);

        bean.setSex(null);
        DictResolver.encode(bean);
        System.out.println(bean);

    }
}
