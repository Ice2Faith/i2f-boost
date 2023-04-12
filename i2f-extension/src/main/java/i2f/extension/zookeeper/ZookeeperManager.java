package i2f.extension.zookeeper;

import i2f.extension.zookeeper.exception.ZookeeperException;
import i2f.extension.zookeeper.watcher.IWatchProcessor;
import i2f.extension.zookeeper.watcher.LoopWatcherAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/15 8:53
 * @desc
 */
@Slf4j
public class ZookeeperManager {
    public static volatile Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

    private volatile ZookeeperConfig config;
    private volatile ZooKeeper zooKeeper;

    public ZookeeperManager() {

    }

    public ZookeeperManager(ZookeeperConfig config) {
        reload(config);
    }

    public void reload(ZookeeperConfig config) {
        try {
            synchronized (ZookeeperManager.class) {
                this.config = config;
                this.zooKeeper = ZookeeperUtil.getConnectedZookeeper(config.getConnectString(),
                        (config.getSessionTimeout() >= 0 ? config.getSessionTimeout() : Integer.MAX_VALUE),
                        new Watcher() {
                            @Override
                            public void process(WatchedEvent event) {
                                if (event.getState() == Event.KeeperState.Expired) {
                                    log.warn("zookeeper session expired,try connect again.");
                                    reload(ZookeeperManager.this.config);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public ZooKeeper zk() {
        if (zooKeeper == null) {
            reload(this.config);
        }
        return zooKeeper;
    }

    public ZookeeperManager of(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
        return this;
    }

    public ZookeeperManager mkdirs(String paths) {
        String[] arr = paths.split("/");
        StringBuilder builder = new StringBuilder();
        for (String item : arr) {
            if ("".equals(item)) {
                continue;
            }
            builder.append("/");
            builder.append(item);
            String path = builder.toString();
            try {
                if (zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (Exception e) {
                throw new ZookeeperException(e.getMessage(), e);
            }
        }
        return this;
    }

    public byte[] obj2ZkData(Object obj) {
        return serializer.serialize(obj);
    }


    public Object zkData2Obj(byte[] data) {
        return serializer.deserialize(data);
    }

    public String parentDir(String path) {
        int idx = path.lastIndexOf("/");
        return path.substring(0, idx);
    }

    public void mkdirsParent(String path) {
        if (path == null || "".equals(path)) {
            return;
        }
        String parent = parentDir(path);
        if (!"".equals(parent)) {
            mkdirs(parent);
        }
    }

    public Object set(String path, Object val) {
        try {
            mkdirsParent(path);
            if (exists(path)) {
                return zooKeeper.setData(path, obj2ZkData(val), -1);
            } else {
                return zooKeeper.create(path, obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public boolean exists(String path) {
        try {
            return zooKeeper.exists(path, false) != null;
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Object get(String path) {
        try {
            byte[] data = zooKeeper.getData(path, false, null);
            return zkData2Obj(data);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Object set(String path, long expire, TimeUnit timeUnit, Object val) {
        try {
            if (exists(path)) {
                zooKeeper.delete(path, -1);
                return zooKeeper.create(path, obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL, null, timeUnit.toMillis(expire));
            } else {
                return zooKeeper.create(path, obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL, null, timeUnit.toMillis(expire));
            }
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Object expire(String path, long expire, TimeUnit timeUnit) {
        try {
            byte[] data = zooKeeper.getData(path, false, null);
            zooKeeper.delete(path, -1);
            return zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL, null, timeUnit.toSeconds(expire));
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Object remove(String path) {
        try {
            zooKeeper.delete(path, -1);
            return true;
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Set<String> keys(String path) {
        try {
            List<String> list = zooKeeper.getChildren(path, false);
            Set<String> ret = new HashSet<>();
            ret.addAll(list);
            return ret;
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public Object clean(String path) {
        try {
            List<String> list = zooKeeper.getChildren(path, false);
            for (String item : list) {
                remove(item);
            }
            return list.size();
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public void watch(String path, Watcher watcher) {
        watch(path, watcher, false);
    }

    public void watch(String path, Watcher watcher, boolean recursive) {
        try {
            zooKeeper.addWatch(path,
                    watcher,
                    recursive ? AddWatchMode.PERSISTENT_RECURSIVE : AddWatchMode.PERSISTENT);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public void watchLoop(String path, IWatchProcessor processor) {
        watchLoop(path, processor, false);
    }

    public void watchLoop(String path, IWatchProcessor processor, boolean recursive) {
        try {
            zooKeeper.addWatch(path,
                    new LoopWatcherAdapter(this, path, processor),
                    recursive ? AddWatchMode.PERSISTENT_RECURSIVE : AddWatchMode.PERSISTENT);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public void mkdirs(String path, boolean temporary) {
        mkdirs(path, "", ZooDefs.Ids.OPEN_ACL_UNSAFE, temporary ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
    }

    public void mkdirs(String path, CreateMode createMode) {
        mkdirs(path, "", ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    public void mkdirs(String path, Object data, CreateMode createMode) {
        mkdirs(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    public void mkdirs(String path, Object data, List<ACL> acl, CreateMode createMode) {
        try {
            mkdirsParent(path);
            zooKeeper.create(path,
                    obj2ZkData(data),
                    acl,
                    createMode);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public void mkdirs(String path, Object data, long ttl) {
        mkdirs(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL, null, ttl);
    }

    public void mkdirs(String path, Object data, CreateMode createMode, long ttl) {
        mkdirs(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode, null, ttl);
    }

    public void mkdirs(String path, Object data, List<ACL> acl, CreateMode createMode, Stat stat, long ttl) {
        try {
            mkdirsParent(path);
            zooKeeper.create(path,
                    obj2ZkData(data),
                    acl,
                    createMode, stat, ttl);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

    public List<OpResult> multi(Collection<Op> ops) {
        try {
            return zooKeeper.multi(ops);
        } catch (Exception e) {
            throw new ZookeeperException(e.getMessage(), e);
        }
    }

}
