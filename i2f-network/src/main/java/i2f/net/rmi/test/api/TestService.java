package i2f.net.rmi.test.api;

import i2f.net.rmi.RmiService;

/**
 * @author Ice2Faith
 * @date 2024/3/12 10:24
 * @desc
 */
public interface TestService extends RmiService {

    String test(String str,int num);
}
