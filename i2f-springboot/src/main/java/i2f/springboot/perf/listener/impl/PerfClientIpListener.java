package i2f.springboot.perf.listener.impl;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.thread.NamingThreadFactory;
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
public class PerfClientIpListener implements ServletRequestListener, PerfSpy {
    public static final String ATTR_CLIENT_IP = "x-client-ip";
    public static final long expireSeconds = 60;

    private ConcurrentHashMap<String, Long> clientMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(3,new NamingThreadFactory("perf","client-ip"));

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
        String ip = ServletContextUtil.getIp(request);
        req.setAttribute(ATTR_CLIENT_IP, ip);
        clientMap.put(ip, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireSeconds));
    }


}
