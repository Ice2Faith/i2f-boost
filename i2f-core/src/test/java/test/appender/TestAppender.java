package test.appender;

import i2f.core.collection.CollectionUtil;
import i2f.core.str.Appender;

/**
 * @author ltb
 * @date 2022/5/7 17:37
 * @desc
 */
public class TestAppender {
    public static void main(String[] args){
        String str = Appender.buffer()
                .add(1)
                .line()
                .addRepeat(2, 3)
                .line()
                .addHexBytes("你好".getBytes())
                .line()
                .addOtcBytes("你好".getBytes())
                .line()
                .addCollection(CollectionUtil.arrayList(1,2,3,4),",","[","]")
                .line()
                .add(",")
                .trimEnd(",")
                .add("|")
                .line()
                .addRepeat("-",20)
                .get();
        System.out.println(str);
    }
}
