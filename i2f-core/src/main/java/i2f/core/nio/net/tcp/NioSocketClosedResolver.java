package i2f.core.nio.net.tcp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author ltb
 * @date 2022/5/11 16:00
 * @desc
 */
public class NioSocketClosedResolver {
    /**
     * java.io.IOException: 远程主机强迫关闭了一个现有的连接。
     * 	at sun.nio.ch.SocketDispatcher.read0(Native Method)
     * 	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43)
     * 	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
     * 	at sun.nio.ch.IOUtil.read(IOUtil.java:197)
     * 	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:380)
     */
    public static boolean isNioClosed(Exception ex){
        if(ex instanceof IOException){
            StackTraceElement elem=ex.getStackTrace()[0];
            if(elem.getClassName().equals("sun.nio.ch.SocketDispatcher")) {
                return true;
            }
        }
        return false;
    }
    public static boolean resolveIoException(SocketChannel channel,IOException ex) throws IOException{
        if(isNioClosed(ex)){
            channel.close();
            return true;
        }
        throw ex;
    }
}
