package i2f.net.rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/12 10:12
 * @desc
 */
public class RmiClient {
    protected InetAddress remoteAddr;
    protected int port=1099;
    protected RMIClientSocketFactory clientSocketFactory;
    protected Registry registry;
    protected AtomicBoolean started=new AtomicBoolean();

    public RmiClient(InetAddress remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public RmiClient(InetAddress remoteAddr, int port) {
        this.remoteAddr = remoteAddr;
        this.port = port;
    }

    public RmiClient(InetAddress remoteAddr, int port, RMIClientSocketFactory clientSocketFactory) {
        this.remoteAddr = remoteAddr;
        this.port = port;
        this.clientSocketFactory = clientSocketFactory;
    }

    public void connect() throws RemoteException {
        if(started.get()){
            return;
        }
        this.registry= LocateRegistry.getRegistry(remoteAddr.getHostAddress(), port,clientSocketFactory);
        started.set(true);
    }

    public<T extends Remote> T getServiceByName(String name) throws RemoteException, NotBoundException {
        return (T)this.registry.lookup(name);
    }

    public<T extends Remote> T getServiceByPath(String path) throws RemoteException, NotBoundException, MalformedURLException {
        String host="0.0.0.0";
        if(remoteAddr!=null){
            host=remoteAddr.getHostAddress();
        }
        return getServiceByPath(host,port,path);
    }

    public static<T extends Remote> T getServiceByPath(String host,int port,String path) throws RemoteException, NotBoundException, MalformedURLException {
        return (T)Naming.lookup(makeRmiUrl(host,port,path));
    }

    public static String makeRmiUrl(String host,int port,String path){
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return "rmi://" + host + ":" + port + "/" + path;
    }

}
