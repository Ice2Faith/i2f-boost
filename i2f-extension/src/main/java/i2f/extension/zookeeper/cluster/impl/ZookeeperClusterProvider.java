package i2f.extension.zookeeper.cluster.impl;

import i2f.extension.zookeeper.ZookeeperManager;
import i2f.extension.zookeeper.cluster.AbsClusterProvider;
import i2f.extension.zookeeper.exception.ZookeeperException;
import i2f.extension.zookeeper.listener.ZookeeperManagerListener;
import i2f.extension.zookeeper.watcher.IWatchProcessor;
import i2f.extension.zookeeper.watcher.LoopWatcherAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2023/4/12 8:42
 * @desc
 */
@Slf4j
public class ZookeeperClusterProvider extends AbsClusterProvider implements IWatchProcessor {
    private ZookeeperManager zookeeperManager;
    private String listenPath = "/cluster";

    private volatile CopyOnWriteArrayList<String> currentGuidList = new CopyOnWriteArrayList<>();

    public ZookeeperClusterProvider(String listenPath, ZookeeperManager zookeeperManager) throws Exception {
        this.listenPath = listenPath;
        this.zookeeperManager = zookeeperManager;
        init();
        this.zookeeperManager.addListener(new ZookeeperManagerListener() {
            @Override
            public void onAfter(ZookeeperManager manager) {
                try {
                    init();
                } catch (Exception e) {
                    throw new ZookeeperException(e.getMessage(), e);
                }
            }
        });
    }

    public void init() throws Exception {

        // init
        zookeeperManager.mkdirs(listenPath);
        // watcher
        log.info("cluster listen :" + listenPath);
        zookeeperManager.watchLoop(listenPath, ZookeeperClusterProvider.this, true);
        // registry
        String path = listenPath + "/" + guid();
        log.info("cluster registry:" + path);
        zookeeperManager.mkdirs(path, true);
    }

    @Override
    public boolean process(WatchedEvent event, LoopWatcherAdapter adapter) {
        Watcher.Event.EventType type = event.getType();
        if (type == Watcher.Event.EventType.NodeCreated
                || type == Watcher.Event.EventType.NodeDeleted
                || type == Watcher.Event.EventType.NodeChildrenChanged) {
            Set<String> keys = zookeeperManager.keys(listenPath);
            log.warn("cluster change:" + StringUtils.collectionToDelimitedString(keys, ","));
            synchronized (currentGuidList) {
                currentGuidList.clear();
                currentGuidList.addAll(keys);
            }
        }
        return true;
    }

    @Override
    protected List<String> getProviderGuidList() {
        return new ArrayList<>(currentGuidList);
    }
}
