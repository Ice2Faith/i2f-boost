package i2f.springboot.perf.listener.impl;

import i2f.springboot.perf.spy.PerfSpy;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/6/8 13:54
 * @desc
 */
public class SpyClientIpListener implements ServletRequestListener, PerfSpy {
    public static final String ATTR_CLIENT_IP = "x-client-ip";
    public static final long expireSeconds = 60;

    private ConcurrentHashMap<String, Long> clientMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

    {
        pool.scheduleWithFixedDelay(() -> {
            long ts = System.currentTimeMillis();
            Set<String> rm = new HashSet<>();
            for (String key : clientMap.keySet()) {
                Long ds = clientMap.get(key);
                if (ds < ts) {
                    rm.add(key);
                }
            }
            for (String key : rm) {
                clientMap.remove(key);
            }
        }, 0, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String name() {
        return "Request:ClientCount";
    }

    @Override
    public double collect() {
        return clientMap.size();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest req = sre.getServletRequest();
        HttpServletRequest request = (HttpServletRequest) req;
        String ip = getIp(request);
        req.setAttribute(ATTR_CLIENT_IP, ip);
        clientMap.put(ip, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireSeconds));
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
}
