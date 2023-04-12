package i2f.extension.zookeeper.cluster;

/**
 * @author Ice2Faith
 * @date 2023/4/11 16:09
 * @desc 集群任务的分配策略
 * 主要使用isMy方法判断是否是自己的方法
 */
public interface ClusterProvider {
    String guid();

    int count();

    int myid();

    boolean isMy(long domainId);
}
