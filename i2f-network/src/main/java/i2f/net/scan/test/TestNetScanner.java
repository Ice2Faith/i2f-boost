package i2f.net.scan.test;

import i2f.net.scan.NetScanner;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ice2Faith
 * @date 2024/3/11 17:57
 * @desc
 */
public class TestNetScanner {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        Set<Integer> rs = NetScanner.portScan(InetAddress.getByName("127.0.0.1"), 0, 1000, pool);
        for (Integer item : rs) {
            System.out.println(item);
        }
        System.out.println("ok");

        Map<InetAddress, Set<Integer>> map = NetScanner.lanScan(InetAddress.getByName("192.168.175.251"), 0, 1000, pool);
        for (Map.Entry<InetAddress, Set<Integer>> entry : map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("ok");

        map = NetScanner.localScan(0, 1000, pool);
        for (Map.Entry<InetAddress, Set<Integer>> entry : map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("ok");
    }
}
