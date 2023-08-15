package i2f.springboot.security.impl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/15 19:08
 * @desc
 */
public interface BeforeLoginChecker {
    void onJsonLogin(String username, String password, Map<String, Object> params, HttpServletRequest request) throws Exception;

    void onFormLogin(String username, String password, HttpServletRequest request) throws Exception;
}
