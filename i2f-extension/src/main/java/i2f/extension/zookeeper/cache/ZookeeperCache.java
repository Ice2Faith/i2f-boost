package i2f.extension.zookeeper.cache;

import i2f.core.cache.ICache;
import i2f.core.json.IJsonProcessor;
import i2f.extension.zookeeper.ZookeeperManager;
import org.apache.zookeeper.ZooKeeper;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/13 8:27
 * @desc
 */
public class ZookeeperCache implements ICache<String, Object> {
    private String prefix = "/cache";
    private ZooKeeper zooKeeper;
    private IJsonProcessor processor;
    private ZookeeperManager manager;

    public ZookeeperCache(ZooKeeper zooKeeper, IJsonProcessor processor) {
        this.prefix = "/cache";
        this.zooKeeper = zooKeeper;
        this.processor = processor;
        manager = new ZookeeperManager(zooKeeper);
        manager.makePaths(prefix);
    }

    public ZookeeperCache(String prefix,ZooKeeper zooKeeper,IJsonProcessor processor){
        this.prefix=prefix;
        this.zooKeeper=zooKeeper;
        this.processor=processor;
        manager=new ZookeeperManager(zooKeeper);
        manager.makePaths(prefix);
    }

    public String getPath(String key){
        if(prefix==null || "".equals(prefix)){
            prefix="/";
        }
        if(key.startsWith("/")){
            key=key.substring(1);
        }
        return prefix+"/"+key;
    }

    @Override
    public void set(String key, Object val) {
        String path = getPath(key);
        manager.set(path, val);
    }

    @Override
    public boolean exists(String key) {
        String path=getPath(key);
        return manager.exists(key);
    }

    @Override
    public Object get(String key) {
        String path=getPath(key);
        return manager.get(path);
    }

    @Override
    public void set(String key, Object val, long expire, TimeUnit timeUnit) {
        String path = getPath(key);
        manager.set(path, expire, timeUnit, val);
    }

    @Override
    public void expire(String key, long expire, TimeUnit timeUnit) {
        String path = getPath(key);
        manager.expire(path, expire, timeUnit);
    }

    @Override
    public void remove(String key) {
        String path = getPath(key);
        manager.remove(path);
    }

    @Override
    public Set<String> keys() {
        return manager.keys(prefix);
    }

    @Override
    public void clean() {
        manager.remove(prefix);
    }
}
