package zookeeper;

import i2f.extension.zookeeper.ZookeeperConfig;
import i2f.extension.zookeeper.ZookeeperManager;
import i2f.extension.zookeeper.cache.ZookeeperCache;
import i2f.extension.zookeeper.watcher.IWatchProcessor;
import i2f.extension.zookeeper.watcher.LoopWatcherAdapter;
import model.TestBean;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/13 9:35
 * @desc
 */
public class TestZookeeper {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperConfig config = new ZookeeperConfig();
        config.setConnectString("127.0.0.1:2181");
        config.setSessionTimeout(Integer.MAX_VALUE);
        ZookeeperManager manager = new ZookeeperManager(config);
        manager.watch("/", new LoopWatcherAdapter(manager, "/", new IWatchProcessor() {
            @Override
            public boolean process(WatchedEvent event, LoopWatcherAdapter adapter) {
                System.out.println(">>>>>>>>>lp:event:" + event.getPath() + ":" + event.getType().name());
                return true;
            }
        }), true);

        ZookeeperCache cache = new ZookeeperCache("/cache", manager);

        TestBean tb = new TestBean();
        tb.setName("zhangsan");
        tb.setAge(23);
        tb.setWeight(61.2);

        cache.set("token_zhangsan",tb);

        boolean ex= cache.exists("token_zhangsan");

        tb.setAge(24);
        cache.set("token_zhangsan",tb,30, TimeUnit.SECONDS);

        if(ex){
            TestBean rtb=(TestBean) cache.get("token_zhangsan");
            System.out.println(rtb);
        }

        cache.remove("token_zhangsan");

        manager.zk().close();
    }
}
