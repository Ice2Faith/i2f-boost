package i2f.extension.zookeeper.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2023/4/11 16:10
 * @desc
 */
public abstract class AbsClusterProvider implements ClusterProvider {
    public static final String GUID = UUID.randomUUID().toString().replaceAll("-", "");

    public static volatile long EXPIRE_TIMOUT_MILLISECOND = 10 * 1000;

    private final CopyOnWriteArrayList<String> cacheGuidList = new CopyOnWriteArrayList<>();
    private final AtomicLong cacheExpireTs = new AtomicLong(0);
    private final AtomicInteger cacheMyId = new AtomicInteger(0);

    protected abstract List<String> getProviderGuidList();

    public void refreshCache() {
        long ts = System.currentTimeMillis();
        if (ts < cacheExpireTs.get()) {
            return;
        }
        List<String> guidList = new ArrayList<>();
        // 获取列表
        List<String> list = getProviderGuidList();
        if (list != null) {
            guidList.addAll(list);
        }
        synchronized (cacheGuidList) {
            cacheExpireTs.set(ts + EXPIRE_TIMOUT_MILLISECOND);
            cacheGuidList.clear();
            cacheGuidList.addAll(guidList);

            cacheGuidList.sort(String::compareTo);
            int id = guidList.indexOf(GUID);
            cacheMyId.set(id);
        }
    }

    @Override
    public String guid() {
        return GUID;
    }

    @Override
    public int count() {
        refreshCache();
        return cacheGuidList.size();
    }

    @Override
    public int myid() {
        refreshCache();
        return cacheMyId.get();
    }

    @Override
    public boolean isMy(long domainId) {
        int cnt = count();
        if (cnt == 0) {
            return true;
        }
        long hcode = Math.abs(domainId) % cnt;
        if (hcode == myid()) {
            return true;
        }
        return false;
    }

}
