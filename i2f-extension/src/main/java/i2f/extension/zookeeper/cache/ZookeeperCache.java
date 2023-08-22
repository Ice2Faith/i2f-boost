package i2f.extension.zookeeper.cache;

import i2f.core.cache.ICache;
import i2f.extension.zookeeper.ZookeeperManager;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/13 8:27
 * @desc
 */
public class ZookeeperCache implements ICache<String, Object> {
    private String prefix = "/cache";
    private ZookeeperManager manager;

    public ZookeeperCache(ZookeeperManager manager) {
        this.prefix = "/cache";
        this.manager = manager;
        manager.mkdirs(prefix);
    }

    public ZookeeperCache(String prefix, ZookeeperManager manager) {
        this.prefix = prefix;
        this.manager = manager;
        manager.mkdirs(prefix);
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
    public Long getExpire(String key, TimeUnit expireUnit) {
        return null;
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
