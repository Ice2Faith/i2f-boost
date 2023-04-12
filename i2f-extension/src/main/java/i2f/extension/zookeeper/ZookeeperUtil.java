package i2f.extension.zookeeper;

import i2f.extension.zookeeper.watcher.ZkConnectWatcherAdapter;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/4/13 8:33
 * @desc
 */
public class ZookeeperUtil {
    public static ZooKeeper getZookeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
        return zooKeeper;
    }

    public static ZooKeeper getConnectedZookeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zooKeeper = getZookeeper(connectString, sessionTimeout, new ZkConnectWatcherAdapter(latch, watcher));
        latch.await();
        return zooKeeper;
    }

}
