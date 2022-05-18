package test;

import test.model.TestBean;

/**
 * @author ltb
 * @date 2022/3/25 21:00
 * @desc
 */
public class Test2Impl implements ITest{
    @Override
    public String say(Object str) {
        return "say2:"+str;
    }

    @Override
    public String bean(TestBean obj) {
        return null;
    }
}
