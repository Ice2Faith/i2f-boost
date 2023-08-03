package i2f.springboot.limit.core;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ice2Faith
 * @date 2023/8/3 9:00
 * @desc
 */
public interface IUserProvider {
    Object getUserKey(HttpServletRequest request);
}
