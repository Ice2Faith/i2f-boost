package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.store.ICacher;
import i2f.core.secret.util.SecretUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/10/19 17:08
 * @desc
 */
public class MemCacher implements ICacher {
    public static Map<String, byte[]> _map = new ConcurrentHashMap<>();
    public ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public String keyOf(byte[] bts) {
        return SecretUtil.toBase64(bts);
    }

    @Override
    public void set(byte[] key, byte[] value) {
        _map.put(keyOf(key), value);
    }

    @Override
    public byte[] get(byte[] key) {
        return _map.get(keyOf(key));
    }

    @Override
    public boolean exists(byte[] key) {
        return _map.containsKey(keyOf(key));
    }

    @Override
    public void remove(byte[] key) {
        _map.remove(keyOf(key));
    }

    @Override
    public void setExpire(byte[] key, byte[] value, long expireSecond) {
        _map.put(keyOf(key), value);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                _map.remove(keyOf(key));
            }
        }, expireSecond, TimeUnit.SECONDS);
    }
}
