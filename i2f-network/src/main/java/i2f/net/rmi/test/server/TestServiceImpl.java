package i2f.net.rmi.test.server;

import i2f.net.rmi.RmiServiceImpl;
import i2f.net.rmi.test.api.TestService;

/**
 * @author Ice2Faith
 * @date 2024/3/12 10:26
 * @desc
 */
public class TestServiceImpl implements TestService, RmiServiceImpl {
    @Override
    public String test(String str, int num) {
        return "rmi:"+str+":"+num;
    }
}
