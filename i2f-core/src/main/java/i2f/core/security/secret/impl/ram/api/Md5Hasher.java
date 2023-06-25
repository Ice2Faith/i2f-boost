package i2f.core.security.secret.impl.ram.api;

import i2f.core.security.secret.api.hash.IHasher;
import i2f.core.security.secret.util.SecretUtil;

/**
 * @author ltb
 * @date 2022/10/19 16:57
 * @desc
 */
public class Md5Hasher implements IHasher {
    @Override
    public byte[] hash(byte[] data) {
        return SecretUtil.md5(data);
    }

}
