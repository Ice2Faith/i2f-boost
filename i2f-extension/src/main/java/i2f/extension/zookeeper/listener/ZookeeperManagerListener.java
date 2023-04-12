package i2f.extension.zookeeper.listener;

import i2f.extension.zookeeper.ZookeeperManager;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author Ice2Faith
 * @date 2023/4/12 15:20
 * @desc
 */
public interface ZookeeperManagerListener {
    default void onBefore(ZookeeperManager manager) {
    }

    default void onAfter(ZookeeperManager manager) {
    }

    default void onEvent(WatchedEvent event, ZookeeperManager manager) {
    }
}
