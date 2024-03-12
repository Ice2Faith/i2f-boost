package i2f.net.rmi.test.server;

import i2f.net.rmi.RmiServer;

/**
 * @author Ice2Faith
 * @date 2024/3/12 10:24
 * @desc
 */
public class TestRmiServer {
    public static void main(String[] args) throws Exception {
        RmiServer rmiServer=new RmiServer(7788);
        rmiServer.listen();

        TestServiceImpl testService=new TestServiceImpl();

        rmiServer.bindByName("testService",testService);

        rmiServer.bindByPath("/services/test",testService);

        System.in.read();
    }
}
