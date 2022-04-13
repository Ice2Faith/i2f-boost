package i2f.extension.zookeeper.watcher;

import org.apache.zookeeper.WatchedEvent;

/**
 * @author ltb
 * @date 2021/9/22
 */
public interface IWatchProcessor {
    boolean process(WatchedEvent event, LoopWatcherAdapter adapter);
}
