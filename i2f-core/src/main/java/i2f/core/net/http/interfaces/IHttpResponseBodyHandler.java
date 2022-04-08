package i2f.core.net.http.interfaces;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/24 14:31
 * @desc
 */
@Author("i2f")
public interface IHttpResponseBodyHandler<T> {
   T readBody(Object ... args);
}
