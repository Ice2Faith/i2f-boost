package i2f.extension.zookeeper.watcher;

import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/4/13 8:43
 * @desc
 */
@Getter
@Setter
public class ZkConnectWatcherAdapter extends ZkConnectWatcher{
    protected Watcher watcher;
    public ZkConnectWatcherAdapter(CountDownLatch latch,Watcher watcher,Object ... args){
        super(latch,args);
        this.watcher=watcher;
    }
    public ZkConnectWatcherAdapter(Watcher watcher,Object ... args){
        super(new CountDownLatch(1),args);
        this.watcher=watcher;
    }
    @Override
    public void processEvent(WatchedEvent event) {
        if(watcher!=null){
            watcher.process(event);
        }
    }
}
