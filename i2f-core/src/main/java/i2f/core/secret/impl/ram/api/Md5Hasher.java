package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.hash.IHasher;
import i2f.core.secret.util.SecretUtil;

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
