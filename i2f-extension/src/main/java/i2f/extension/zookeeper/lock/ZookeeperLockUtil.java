package i2f.extension.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author ltb
 * @date 2022/4/15 8:30
 * @desc
 */
public class ZookeeperLockUtil {
    public static ZookeeperInterMutexLock getZkMutexLock(String path,CuratorFramework client){
        return new ZookeeperInterMutexLock(getMutexLock(path, client));
    }
    public static ZookeeperInterMutexLock getZkMutexLock(String path,String connectString,int sessionTimeout){
        return new ZookeeperInterMutexLock(getMutexLock(path, connectString, sessionTimeout));
    }
    public static InterProcessMutex getMutexLock(String path,String connectString,int sessionTimeout){
        return getMutexLock(path,getClient(connectString, sessionTimeout));
    }
    public static InterProcessMutex getMutexLock(String path, CuratorFramework client){
        return new InterProcessMutex(client,path);
    }
    public static CuratorFramework getClient(String connectString,int sessionTimeout){
        ExponentialBackoffRetry retry=new ExponentialBackoffRetry(3000,3);

        CuratorFramework client= CuratorFrameworkFactory
                .builder()
                .connectString(connectString)
                .connectionTimeoutMs(3000)
                .sessionTimeoutMs(sessionTimeout)
                .retryPolicy(retry)
                .build();

        client.start();

        return client;
    }
}
