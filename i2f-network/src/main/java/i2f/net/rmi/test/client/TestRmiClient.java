package i2f.net.rmi.test.client;

import i2f.net.rmi.RmiClient;
import i2f.net.rmi.test.api.TestService;

import java.net.InetAddress;

/**
 * @author Ice2Faith
 * @date 2024/3/12 10:28
 * @desc
 */
public class TestRmiClient {
    public static void main(String[] args) throws Exception {
        RmiClient rmiClient=new RmiClient(InetAddress.getByName("127.0.0.1"),7788);
        rmiClient.connect();

        TestService nameService = rmiClient.getServiceByName("testService");

        System.out.println(nameService.test("name", 1));

        TestService pathService=rmiClient.getServiceByPath("services/test");

        System.out.println(pathService.test("path", 2));
    }
}
