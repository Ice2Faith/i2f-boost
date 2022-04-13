package i2f.extension.zookeeper.cache;

import i2f.core.cache.ICache;
import i2f.core.collection.CollectionUtil;
import i2f.core.json.IJsonProcessor;
import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.zookeeper.exception.ZookeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/13 8:27
 * @desc
 */
public class ZookeeperCache implements ICache<String> {
    private String prefix="/cache";
    private ZooKeeper zooKeeper;
    private IJsonProcessor processor;

    public ZookeeperCache(ZooKeeper zooKeeper,IJsonProcessor processor){
        this.prefix="/cache";
        this.zooKeeper=zooKeeper;
        this.processor=processor;
        initNodes();
    }

    public ZookeeperCache(String prefix,ZooKeeper zooKeeper,IJsonProcessor processor){
        this.prefix=prefix;
        this.zooKeeper=zooKeeper;
        this.processor=processor;
        initNodes();
    }

    public void initNodes(){
        String[] arr=prefix.split("/");
        StringBuilder builder=new StringBuilder();
        for(String item : arr){
            if("".equals(item)){
                continue;
            }
            builder.append("/");
            builder.append(item);
            String path= builder.toString();
            try{
                if(zooKeeper.exists(path,false)==null){
                    zooKeeper.create(path,item.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }catch(Exception e){
                throw new ZookeeperException(e.getMessage(),e);
            }
        }
    }

    public String obj2Json(Object obj){
        if(obj==null){
            return "null:"+"null";
        }
        Class clazz=obj.getClass();
        return clazz.getName()+":"+processor.toText(obj);
    }

    public byte[] obj2ZkData(Object obj){
        try{
            String fjs=obj2Json(obj);
            return fjs.getBytes("UTF-8");
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public String zkData2Json(byte[] data){
        try{
            return new String(data,"UTF-8");
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object zkData2Obj(byte[] data){
        String fjs=zkData2Json(data);
        int idx=fjs.indexOf(":");
        String className=fjs.substring(0,idx);
        String json=fjs.substring(idx+1);
        if("null".equals(className)){
            return null;
        }
        Class clazz= ReflectResolver.getClazz(className);
        return processor.parseText(json,clazz);
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
    public Object set(String key, Object val) {
        String path=getPath(key);
        try{
            if(exists(key)){
                return zooKeeper.setData(path,obj2ZkData(val),-1);
            }else{
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public boolean exists(String key) {
        try{
            String path=getPath(key);
            return zooKeeper.exists(path,false)!=null;
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Object get(String key) {
        String path=getPath(key);
        try{
            byte[] data=zooKeeper.getData(path,false,null);
            return zkData2Obj(data);
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Object set(String key, long expire, TimeUnit timeUnit, Object val) {
        String path=getPath(key);
        try{
            if(exists(key)){
                zooKeeper.delete(path,-1);
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toMillis(expire));
            }else{
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toMillis(expire));
            }
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Object expire(String key, long expire, TimeUnit timeUnit) {
        String path=getPath(key);
        try{
            byte[] data=zooKeeper.getData(path,false,null);
            zooKeeper.delete(path,-1);
            return zooKeeper.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toSeconds(expire));
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Object remove(String key) {
        String path=getPath(key);
        try{
            zooKeeper.delete(path,-1);
            return true;
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Set<String> keys() {
        try{
            List<String> list=zooKeeper.getChildren(prefix,false);
            return CollectionUtil.hashSet(list);
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    @Override
    public Object clean() {
        try{
            List<String> list=zooKeeper.getChildren(prefix,false);
            for(String item : list){
                remove(item);
            }
            return list.size();
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }
}
