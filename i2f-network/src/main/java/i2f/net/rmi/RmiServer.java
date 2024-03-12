package i2f.net.rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/12 9:56
 * @desc
 * 针对bind的rmi远程service实现
 * 不仅需要实现Remote这个空接口
 * 还需要实现Serializable这个空接口
 * 因为这个实际上是将实现类传输到client端进行调用的，所以需要能够序列化
 * 针对这个情况，提供了两个空接口
 * RmiService 对应service的接口声明
 * RmiServiceImpl 对应service的实现
 */
public class RmiServer {
    protected InetAddress bindAddr;
    protected int port=1099;
    protected RMIClientSocketFactory clientSocketFactory;
    protected RMIServerSocketFactory serverSocketFactory;
    protected Registry registry;
    protected AtomicBoolean started=new AtomicBoolean();

    public RmiServer() {
    }

    public RmiServer(int port) {
        this.port = port;
    }

    public RmiServer(InetAddress bindAddr, int port) {
        this.bindAddr = bindAddr;
        this.port = port;
    }

    public RmiServer(InetAddress bindAddr, int port,
                     RMIClientSocketFactory clientSocketFactory,
                     RMIServerSocketFactory serverSocketFactory) {
        this.bindAddr = bindAddr;
        this.port = port;
        this.clientSocketFactory = clientSocketFactory;
        this.serverSocketFactory = serverSocketFactory;
    }

    public void listen() throws RemoteException {
        if(started.get()){
            return;
        }
        this.registry= LocateRegistry.createRegistry(port,clientSocketFactory,serverSocketFactory);
        started.set(true);
    }

    public void unbindByName(String name) throws RemoteException, NotBoundException {
        this.registry.unbind(name);
    }

    public<T extends Remote> void bindByName(String name,T service) throws RemoteException {
        this.registry.rebind(name,service);
    }

    public void unbindByPath(String path) throws RemoteException, NotBoundException, MalformedURLException {
        String host="0.0.0.0";
        if(bindAddr!=null){
            host=bindAddr.getHostAddress();
        }
        Naming.unbind(RmiClient.makeRmiUrl(host,port,path));
    }

    public<T extends Remote> void bindByPath(String path,T service) throws RemoteException, MalformedURLException {
        String host="0.0.0.0";
        if(bindAddr!=null){
            host=bindAddr.getHostAddress();
        }
        Naming.rebind(RmiClient.makeRmiUrl(host,port,path), service);
    }

}
