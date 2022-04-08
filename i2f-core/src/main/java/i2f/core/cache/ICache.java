package i2f.core.cache;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/3/22 19:49
 * @desc
 */
@Author("i2f")
@Remark("defined cache provider")
public interface ICache<T> {
    Object set(@Name("key") T key, @Name("val")Object val);
    boolean exists(@Name("key")T key);
    Object get(@Name("key")T key);
    Object set(@Name("key")T key, @Name("expire")long expire, @Name("timeUnit")TimeUnit timeUnit,@Name("val")Object val);
    Object expire(@Name("key")T key,@Name("expire")long expire,@Name("timeUnit")TimeUnit timeUnit);
    Object remove(@Name("key")T key);
    Set<T> keys();
    Object clean();
}
