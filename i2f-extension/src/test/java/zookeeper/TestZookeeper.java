package zookeeper;

import i2f.extension.json.gson.GsonJsonProcessor;
import i2f.extension.zookeeper.ZookeeperUtil;
import i2f.extension.zookeeper.cache.ZookeeperCache;
import i2f.extension.zookeeper.watcher.IWatchProcessor;
import i2f.extension.zookeeper.watcher.LoopWatcherAdapter;
import model.TestBean;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/13 9:35
 * @desc
 */
public class TestZookeeper {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper= ZookeeperUtil.getConnectedZookeeper("127.0.0.1:2181", Integer.MAX_VALUE, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(">>>>>>>>>>event:"+event.getPath()+":"+event.getType().name());
            }
        });
        zooKeeper.addWatch("/",new LoopWatcherAdapter(zooKeeper, "/", new IWatchProcessor() {
            @Override
            public boolean process(WatchedEvent event, LoopWatcherAdapter adapter) {
                System.out.println(">>>>>>>>>lp:event:"+event.getPath()+":"+event.getType().name());
                return true;
            }
        }), AddWatchMode.PERSISTENT_RECURSIVE);

        ZookeeperCache cache=new ZookeeperCache(zooKeeper,new GsonJsonProcessor());

        TestBean tb=new TestBean();
        tb.setName("zhangsan");
        tb.setAge(23);
        tb.setWeight(61.2);

        cache.set("token_zhangsan",tb);

        boolean ex= cache.exists("token_zhangsan");

        tb.setAge(24);
        cache.set("token_zhangsan",30, TimeUnit.SECONDS,tb);

        if(ex){
            TestBean rtb=(TestBean) cache.get("token_zhangsan");
            System.out.println(rtb);
        }

        cache.remove("token_zhangsan");

        zooKeeper.close();
    }
}
