package i2f.extension.guarder;

import i2f.core.cache.ICache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/21 9:34
 * @desc
 */
@Data
@Component
public class LoginGuarder {

    public static final String LOCK_USERNAME_PREFIX = "login:guarder:lock:username:";
    public static final String LOCK_IP_PREFIX = "login:guarder:lock:ip:";

    private boolean enableUsernameGuarder = true;
    private int usernameFailureCount = 5;
    private int usernameLockedSeconds = 30 * 60;

    private boolean enableIpGuarder = true;
    private int ipFailureCount = 30;
    private int ipLockedSeconds = 30 * 60;
    @Autowired
    private ICache<String, Object> cache;

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public String usernameCacheKey(String username) {
        return LOCK_USERNAME_PREFIX + username;
    }

    public String ipCacheKey(String ip) {
        return LOCK_IP_PREFIX + ip;
    }

    public Long lockedUsernameTime(String username, TimeUnit expireUnit) {
        return cache.getExpire(usernameCacheKey(username), expireUnit);
    }

    public Long lockedIpTime(HttpServletRequest request, TimeUnit expireUnit) {
        String ip = getIp(request);
        return cache.getExpire(ipCacheKey(ip), expireUnit);
    }

    public LockType check(HttpServletRequest request, String username) {
        if (enableUsernameGuarder) {
            Object obj = cache.get(usernameCacheKey(username));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    Integer cnt = Integer.valueOf(str);
                    if (cnt >= usernameFailureCount) {
                        return LockType.USERNAME;
                    }
                }
            }
        }
        if (enableIpGuarder) {
            String ip = getIp(request);
            Object obj = cache.get(ipCacheKey(ip));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    Integer cnt = Integer.valueOf(str);
                    if (cnt >= ipFailureCount) {
                        return LockType.IP;
                    }
                }
            }
        }
        return LockType.NONE;
    }

    public void failure(HttpServletRequest request, String username) {
        if (enableUsernameGuarder) {
            int cnt = 0;
            Object obj = cache.get(usernameCacheKey(username));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    cnt = Integer.valueOf(str);
                }
            }
            cnt++;
            cache.set(usernameCacheKey(username), cnt + "", usernameLockedSeconds, TimeUnit.SECONDS);
        }
        if (enableIpGuarder) {
            int cnt = 0;
            String ip = getIp(request);
            Object obj = cache.get(ipCacheKey(ip));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    cnt = Integer.valueOf(str);
                }
            }
            cnt++;
            cache.set(ipCacheKey(ip), cnt + "", ipLockedSeconds, TimeUnit.SECONDS);
        }
    }

    public void success(HttpServletRequest request, String username) {
        if (enableUsernameGuarder) {
            cache.remove(usernameCacheKey(username));
        }
        if (enableIpGuarder) {
            String ip = getIp(request);
            cache.remove(ipCacheKey(ip));
        }
    }


    public enum LockType {
        NONE, USERNAME, IP;
    }
}
