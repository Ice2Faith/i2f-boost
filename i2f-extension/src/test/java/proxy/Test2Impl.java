package proxy;

/**
 * @author ltb
 * @date 2022/3/25 21:00
 * @desc
 */
public class Test2Impl implements ITest{
    @Override
    public String say(String str) {
        if(str!=null){
            throw new IllegalStateException("str must is null");
        }
        return "say2:"+str;
    }
}
