package i2f.extension.zookeeper.watcher;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author ltb
 * @date 2021/9/22
 */
@Data
@Slf4j
public class LoopWatcherAdapter extends AbsLoopWatcher {
    protected ZooKeeper keeper;
    protected String path;
    protected IWatchProcessor processor;
    private Watcher watcher;

    public LoopWatcherAdapter(ZooKeeper keeper,String path){
        this.keeper=keeper;
        this.path=path;
    }

    public LoopWatcherAdapter(ZooKeeper keeper, String path, IWatchProcessor processor) {
        this.keeper = keeper;
        this.path = path;
        this.processor = processor;
    }

    public LoopWatcherAdapter(ZooKeeper keeper, String path, Watcher watcher) {
        this.keeper = keeper;
        this.path = path;
        this.watcher = watcher;
    }

    @Override
    public boolean onProcess(WatchedEvent event) {
        if(processor!=null){
            return processor.process(event,this);
        }else if(watcher!=null){
            watcher.process(event);
        }
        return true;
    }

    @Override
    public ZooKeeper getZooKeeper() {
        return keeper;
    }

    @Override
    public String getPath() {
        return path;
    }
}
