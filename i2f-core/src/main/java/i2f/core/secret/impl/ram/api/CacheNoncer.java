package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.nonce.INoncer;
import i2f.core.secret.api.store.ICacher;
import i2f.core.secret.util.SecretUtil;

/**
 * @author ltb
 * @date 2022/10/19 17:04
 * @desc
 */
public class CacheNoncer implements INoncer {
    public ICacher _cache = new MemCacher();
    public long nonceTimeoutSecond = 30 * 60;
    public static final byte[] NONCE_HOLDER = new byte[]{32};

    public CacheNoncer() {
    }

    public CacheNoncer(ICacher _cache) {
        this._cache = _cache;
    }

    @Override
    public byte[] nonce() {
        String str = SecretUtil.uuid();
        return SecretUtil.str2utf8(str);
    }

    @Override
    public boolean pass(byte[] nonce) {
        return !_cache.exists(nonce);
    }

    @Override
    public void store(byte[] nonce) {
        _cache.setExpire(nonce, NONCE_HOLDER, nonceTimeoutSecond);
    }
}
