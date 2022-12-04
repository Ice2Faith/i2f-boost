package i2f.extension.zookeeper;

import i2f.core.collection.Collections;
import i2f.core.text.FormatTextSerializer;
import i2f.core.text.ITextSerializer;
import i2f.extension.json.gson.GsonJsonProcessor;
import i2f.extension.zookeeper.exception.ZookeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/15 8:53
 * @desc
 */
public class ZookeeperManager {
    private ITextSerializer serializer=new FormatTextSerializer(new GsonJsonProcessor());
    private ZooKeeper zooKeeper;
    public ZookeeperManager(){

    }
    public ZookeeperManager(ZooKeeper zooKeeper){
        this.zooKeeper=zooKeeper;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public ZookeeperManager setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
        return this;
    }


    public ZookeeperManager makePaths(String paths){
        String[] arr=paths.split("/");
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
                    zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }catch(Exception e){
                throw new ZookeeperException(e.getMessage(),e);
            }
        }
        return this;
    }


    public byte[] obj2ZkData(Object obj){
        return serializer.serialize(obj);
    }


    public Object zkData2Obj(byte[] data){
       return serializer.deserialize(data);
    }

    public String parentPath(String path){
        int idx=path.lastIndexOf("/");
        return path.substring(0,idx);
    }
    public void makeParentPaths(String path){
        if(path==null || "".equals(path)){
            return;
        }
        String parent=parentPath(path);
        if(!"".equals(parent)){
            makePaths(parent);
        }
    }

    public Object set(String path, Object val) {
        try{
            makeParentPaths(path);
            if(exists(path)){
                return zooKeeper.setData(path,obj2ZkData(val),-1);
            }else{
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public boolean exists(String path) {
        try{
            return zooKeeper.exists(path,false)!=null;
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object get(String path) {
        try{
            byte[] data=zooKeeper.getData(path,false,null);
            return zkData2Obj(data);
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object set(String path, long expire, TimeUnit timeUnit, Object val) {
        try{
            if(exists(path)){
                zooKeeper.delete(path,-1);
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toMillis(expire));
            }else{
                return zooKeeper.create(path,obj2ZkData(val), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toMillis(expire));
            }
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object expire(String path, long expire, TimeUnit timeUnit) {
        try{
            byte[] data=zooKeeper.getData(path,false,null);
            zooKeeper.delete(path,-1);
            return zooKeeper.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_WITH_TTL,null,timeUnit.toSeconds(expire));
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object remove(String path) {
        try{
            zooKeeper.delete(path,-1);
            return true;
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Set<String> keys(String path) {
        try{
            List<String> list=zooKeeper.getChildren(path,false);
            return Collections.hashSet(list);
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }

    public Object clean(String path) {
        try{
            List<String> list=zooKeeper.getChildren(path,false);
            for(String item : list){
                remove(item);
            }
            return list.size();
        }catch(Exception e){
            throw new ZookeeperException(e.getMessage(),e);
        }
    }


}
