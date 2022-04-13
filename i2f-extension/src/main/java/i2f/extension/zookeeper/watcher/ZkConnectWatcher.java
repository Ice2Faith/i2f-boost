package i2f.extension.zookeeper.watcher;

import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/4/13 8:35
 * @desc
 */
@Getter
@Setter
public abstract class ZkConnectWatcher implements Watcher {
    protected CountDownLatch latch;
    protected Object[] args;
    public ZkConnectWatcher(CountDownLatch latch,Object ... args){
        this.latch=latch;
        this.args=args;
    }
    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected==event.getState()){
            onConnected(event);
        }
        processEvent(event);
    }

    public void onConnected(WatchedEvent event){
        if(latch!=null){
            latch.countDown();
        }
    }

    public abstract void processEvent(WatchedEvent event);
}
