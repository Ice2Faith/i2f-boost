package text;

import i2f.core.serialize.str.text.FormatTextSerializer;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import model.TestBean;

/**
 * @author ltb
 * @date 2022/4/14 10:41
 * @desc
 */
public class TestFormatTextSerializer {
    public static void main(String[] args){
        FormatTextSerializer serializer = new FormatTextSerializer(new JacksonJsonSerializer());
        String text = null;
        Object val=null;

        TestBean bean=new TestBean();
        bean.setName("zhangsan");
        bean.setAge(23);
        bean.setWeight(61.2);


        doTest(serializer,12);
        doTest(serializer,true);
        doTest(serializer,12.125);
        doTest(serializer,"");
        doTest(serializer,null);
        doTest(serializer,"hahaha");
        doTest(serializer,'A');
        doTest(serializer,bean);

        TestBean nb=(TestBean) serializer.serializeCopy(bean);
        System.out.println(nb);
    }

    private static void doTest(FormatTextSerializer serializer,Object obj){
        String text=null;
        Object val=null;
        System.out.println("------------------");
        text = serializer.serialize(obj);
        System.out.println(text);
        val = serializer.deserialize(text, null);
        System.out.println(val);
    }
}
