package i2f.core.jce.md;

import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 17:53
 * @desc
 */
public interface IMessageDigestor {
    byte[] mds(byte[] data) throws Exception;
    byte[] mds(InputStream is) throws Exception;
}
