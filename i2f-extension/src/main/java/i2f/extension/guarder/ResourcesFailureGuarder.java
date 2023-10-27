package i2f.extension.guarder;

import i2f.core.cache.ICache;
import lombok.Data;

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
public class ResourcesFailureGuarder {

    public static final String LOCK_RESOURCES_PREFIX = "guarder:lock:resources:";
    public static final String LOCK_IP_PREFIX = "guarder:lock:ip:";

    protected String resourcesType ="default";

    protected boolean enableResourcesGuarder = true;
    protected int resourcesFailureCount = 5;
    protected int resourcesLockedSeconds = 30 * 60;

    protected boolean enableIpGuarder = true;
    protected int ipFailureCount = 30;
    protected int ipLockedSeconds = 30 * 60;

    private ICache<String, Object> cache;

    public ResourcesFailureGuarder() {
    }

    public ResourcesFailureGuarder(String resourcesType) {
        this.resourcesType = resourcesType;
    }

    public ResourcesFailureGuarder(String resourcesType, ICache<String, Object> cache) {
        this.resourcesType = resourcesType;
        this.cache = cache;
    }

    public ResourcesFailureGuarder(String resourcesType, ICache<String, Object> cache, boolean enableResourcesGuarder, boolean enableIpGuarder) {
        this.resourcesType = resourcesType;
        this.cache = cache;
        this.enableResourcesGuarder = enableResourcesGuarder;
        this.enableIpGuarder = enableIpGuarder;
    }

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
            if ("127.0.0.1".equals(ip)) {
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

    public static String getIpKey(HttpServletRequest request) {
        String ip = getIp(request);
        if(ip!=null){
            ip=ip.replaceAll(":","-");
        }
        return ip;
    }

    public String resourcesCacheKey(String resources) {
        return LOCK_RESOURCES_PREFIX + resourcesType +":" + resources;
    }

    public String ipCacheKey(String ip) {
        return LOCK_IP_PREFIX+ resourcesType +":" + ip;
    }

    public Long lockedResourcesTime(String resources, TimeUnit expireUnit) {
        return cache.getExpire(resourcesCacheKey(resources), expireUnit);
    }

    public Long lockedIpTime(HttpServletRequest request, TimeUnit expireUnit) {
        String ip = getIpKey(request);
        return cache.getExpire(ipCacheKey(ip), expireUnit);
    }

    public LockType check(HttpServletRequest request, String resources) {
        if (enableResourcesGuarder) {
            Object obj = cache.get(resourcesCacheKey(resources));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    Integer cnt = Integer.valueOf(str);
                    if (cnt >= resourcesFailureCount) {
                        return LockType.RESOURCES;
                    }
                }
            }
        }
        if (enableIpGuarder) {
            String ip = getIpKey(request);
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

    public void failure(HttpServletRequest request, String resources) {
        if (enableResourcesGuarder) {
            int cnt = 0;
            Object obj = cache.get(resourcesCacheKey(resources));
            if (obj != null) {
                String str = String.valueOf(obj);
                if (!"".equals(str)) {
                    cnt = Integer.valueOf(str);
                }
            }
            cnt++;
            cache.set(resourcesCacheKey(resources), cnt + "", resourcesLockedSeconds, TimeUnit.SECONDS);
        }
        if (enableIpGuarder) {
            int cnt = 0;
            String ip = getIpKey(request);
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

    public void success(HttpServletRequest request, String resources) {
        if (enableResourcesGuarder) {
            cache.remove(resourcesCacheKey(resources));
        }
        if (enableIpGuarder) {
            String ip = getIpKey(request);
            cache.remove(ipCacheKey(ip));
        }
    }


    public enum LockType {
        NONE, RESOURCES, IP;
    }
}
