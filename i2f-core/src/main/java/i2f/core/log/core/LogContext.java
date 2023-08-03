package i2f.core.log.core;

import i2f.core.log.data.LogDto;
import i2f.core.log.impl.SysoutLogWriter;
import i2f.core.serialize.str.IStringObjectSerializer;
import i2f.core.serialize.str.json.impl.Json2Serializer;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/8/2 14:45
 * @desc
 */
public class LogContext {
    public static final String DEFAULT_WRITER_KEY="default";

    public static LinkedBlockingQueue<LogDto> queue=new LinkedBlockingQueue<>();
    public static AtomicBoolean initialed=new AtomicBoolean(false);
    public static AtomicInteger maxQueue=new AtomicInteger(-1);
    public static Map<String,ILogWriter> writers=new ConcurrentHashMap<>();

    public static AtomicBoolean canShutdown=new AtomicBoolean(true);

    public static  String system;
    public static IStringObjectSerializer serializer=new Json2Serializer();

    public static AtomicBoolean defaultBefore=new AtomicBoolean(false);
    public static AtomicBoolean defaultAfter=new AtomicBoolean(false);
    public static AtomicBoolean defaultThrowing=new AtomicBoolean(true);

    static {
        try{
            String hostName = InetAddress.getLocalHost().getHostName();
            system=hostName;
        }catch(Exception e){

        }
        writers.put(DEFAULT_WRITER_KEY,new SysoutLogWriter());
    }

}
