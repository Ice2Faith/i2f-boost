package i2f.core.rmi;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author ltb
 * @date 2022/5/28 18:18
 * @desc rmi远程方法调用
 * 服务端徐亚监听指定的端口
 * 通过服务名是svcName（相当于beanId）方式提供服务，需要客户端先获得Registry链接对象再根据名称查找服务
 * 或者通过naming(相当于restful接口)的方式提供服务，客户端不再需要获得Registry对象，由于是URL方式，系统内部自动解析
 *
 */
public class RmiManager {
    public static final int DEFAULT_RMI_LISTEN_PORT = 1099;

    public static Registry listen(int port) throws RemoteException {
        return LocateRegistry.createRegistry(port);
    }

    public static Registry listen() throws RemoteException {
        return listen(DEFAULT_RMI_LISTEN_PORT);
    }

    public static <T extends Remote> void registryService(Registry registry, String svcName, T svc) throws RemoteException {
        registry.rebind(svcName, svc);
    }

    public static Registry connect(String host, int port) throws RemoteException {
        return LocateRegistry.getRegistry(host, port);
    }

    public static Registry connect(String host) throws RemoteException {
        return connect(host, DEFAULT_RMI_LISTEN_PORT);
    }

    public static <T extends Remote> T findService(Registry registry, String svcName) throws RemoteException, NotBoundException {
        return (T) registry.lookup(svcName);
    }

    //////////////////////////////////////////////

    public static <T extends Remote> void registryNamingService(int port, String path, T svc) throws RemoteException, MalformedURLException, AlreadyBoundException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Naming.bind("rmi://0.0.0.0:" + port + "/" + path, svc);
    }

    public static <T extends Remote> void registryNamingService(String path, T svc) throws RemoteException, MalformedURLException, AlreadyBoundException {
        registryNamingService(DEFAULT_RMI_LISTEN_PORT, path, svc);
    }

    public static <T extends Remote> T findNamingService(String host, int port, String path) throws RemoteException, NotBoundException, MalformedURLException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String url = "rmi://" + host + ":" + port + "/" + path;
        return (T) Naming.lookup(url);
    }

    public static <T extends Remote> T findNamingService(String host, String path) throws RemoteException, NotBoundException, MalformedURLException {
        return findNamingService(host, DEFAULT_RMI_LISTEN_PORT, path);
    }
}
