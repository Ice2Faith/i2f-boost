package i2f.extension.netty.tcp;

/**
 * @author ltb
 * @date 2022/6/24 18:59
 * @desc
 */
public class TestNettyTcpServer {
    public static void main(String[] args) throws InterruptedException {
        NettyTcpUtil.starterServer(9110);
    }
}
