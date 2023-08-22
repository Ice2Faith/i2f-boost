package i2f.springboot.security.def.guarder;

import i2f.core.exception.BoostException;
import i2f.extension.guarder.LoginGuarder;
import i2f.springboot.security.impl.BeforeLoginChecker;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/22 21:46
 * @desc
 */
@ConditionalOnBean(LoginGuarder.class)
@Data
@Component
public class LoginLockBeforeLoginChecker implements BeforeLoginChecker {

    @Autowired
    private LoginGuarder loginGuarder;

    @Override
    public void onJsonLogin(String username, String password, Map<String, Object> params, HttpServletRequest request) throws Exception {
        onLogin(username, request);
    }

    @Override
    public void onFormLogin(String username, String password, HttpServletRequest request) throws Exception {
        onLogin(username, request);
    }

    public void onLogin(String username, HttpServletRequest request) {
        LoginGuarder.LockType type = loginGuarder.check(request, username);
        if (type == LoginGuarder.LockType.USERNAME) {
            throw new BoostException("您的账号已被锁定，请在" +
                    loginGuarder.getCache().getExpire(
                            loginGuarder.usernameCacheKey(username),
                            TimeUnit.SECONDS) +
                    "秒后重试");
        } else if (type == LoginGuarder.LockType.IP) {
            String ip = LoginGuarder.getIp(request);
            throw new BoostException("您的IP已被锁定，请在" +
                    loginGuarder.getCache().getExpire(
                            loginGuarder.ipCacheKey(ip),
                            TimeUnit.SECONDS) +
                    "秒后重试");
        }
    }
}
