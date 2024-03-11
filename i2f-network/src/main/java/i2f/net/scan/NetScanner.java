package i2f.net.scan;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/3/11 17:03
 * @desc
 */
public class NetScanner {

    public static final Predicate<NetworkInterface> FILTER_USEFUL_NETWORK_INTERFACE=(networkInterface)->{
        try{
            if(networkInterface.isLoopback()){
                return false;
            }
            if(!networkInterface.isUp()){
                return false;
            }
        }catch(Exception e){
            return false;
        }
        return true;
    };

    public static final Predicate<InetAddress> FILTER_USEFUL_INET_ADDRESS=(inetAddress)->{
        if(inetAddress.isLoopbackAddress()){
            return false;
        }
        if(inetAddress.isMulticastAddress()){
            return false;
        }
        return true;
    };

    public static Set<NetworkInterface> getNetworkInterfaces(Predicate<NetworkInterface> filter) throws IOException {
        Set<NetworkInterface> ret=new LinkedHashSet<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if(filter==null || filter.test(networkInterface)){
                ret.add(networkInterface);
            }
        }
        return ret;
    }

    public static Set<NetworkInterface> getUsefulNetworkInterfaces() throws IOException {
        return getNetworkInterfaces(FILTER_USEFUL_NETWORK_INTERFACE);
    }

    public static Set<InetAddress> getInetAddresses(NetworkInterface networkInterface,Predicate<InetAddress> filter){
        Set<InetAddress> ret=new LinkedHashSet<>();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while(inetAddresses.hasMoreElements()){
            InetAddress inetAddress = inetAddresses.nextElement();
            if(filter==null || filter.test(inetAddress)){
                ret.add(inetAddress);
            }
        }
        return ret;
    }

    public static Set<InetAddress> getUsefulInetAddresses(NetworkInterface networkInterface){
        return getInetAddresses(networkInterface,FILTER_USEFUL_INET_ADDRESS);
    }

    public static Set<InetAddress> getLocalAddresses(Predicate<NetworkInterface> networkInterfaceFilter,
                                                     Predicate<InetAddress> inetAddressFilter) throws IOException {
        Set<InetAddress> ret=new LinkedHashSet<>();
        Set<NetworkInterface> networkInterfaces = getNetworkInterfaces(networkInterfaceFilter);
        for (NetworkInterface networkInterface : networkInterfaces) {
            Set<InetAddress> inetAddresses = getInetAddresses(networkInterface, inetAddressFilter);
            ret.addAll(inetAddresses);
        }
        return ret;
    }

    public static Set<InetAddress> getLocalUsefulInetAddresses() throws IOException {
        return getLocalAddresses(FILTER_USEFUL_NETWORK_INTERFACE,FILTER_USEFUL_INET_ADDRESS);
    }

    public static Map<InetAddress,Set<Integer>> localScan(int minPort,int maxPort,ExecutorService pool) throws IOException {
        Set<InetAddress> addrs=getLocalUsefulInetAddresses();

        Map<InetAddress,Set<Integer>> ret=new HashMap<>();
        for (InetAddress addr : addrs) {
            Map<InetAddress, Set<Integer>> map = lanScan(addr, minPort, maxPort, pool);
            ret.putAll(map);
        }
        return ret;
    }
    public static Map<InetAddress,Set<Integer>> lanScan(InetAddress addr,int minPort,int maxPort,ExecutorService pool) throws IOException {
        ConcurrentHashMap<InetAddress,Set<Integer>> ret=new ConcurrentHashMap<>();
        byte[] address = addr.getAddress();
        int count=255;
        CountDownLatch latch=new CountDownLatch(count);
        for (int i = 1; i < 256; i++) {
            address[address.length-1]=(byte)i;
            InetAddress nextAddr = InetAddress.getByAddress(address);
            pool.submit(()->{
                try {
                    if(!nextAddr.isReachable(1000)){
                        return;
                    }
                    Set<Integer> ports = portScan(nextAddr, minPort, maxPort, pool);
                    if(!ports.isEmpty()){
                        ret.put(nextAddr,ports);
                    }
                }catch (IOException e){

                }finally {
                    latch.countDown();
                }
            });
        }
        try{
            latch.await();
        }catch(Exception e){

        }
        return ret;
    }
    public static Set<Integer> portScan(InetAddress addr, int minPort, int maxPort, ExecutorService pool){
        ConcurrentHashMap<Integer,Boolean> ret=new ConcurrentHashMap<>();
        int count=maxPort-minPort+1;
        CountDownLatch latch=new CountDownLatch(count);
        for (int i = minPort; i <= maxPort; i++) {
            int port=i;
            pool.submit(()->{
                try{
                    System.out.println("scan:"+addr+":"+port);
                    Socket sock=new Socket(addr,port);
                    if(sock.isConnected()){
                        ret.put(port,true);
                    }
                    sock.close();
                }catch(Exception e){

                }finally {
                    latch.countDown();
                }
            });
        }
        try{
            latch.await();
        }catch(Exception e){

        }
        return new TreeSet<>(ret.keySet());
    }
}
