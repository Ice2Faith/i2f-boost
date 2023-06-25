package i2f.core.zplugin.log.impl;

import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.zplugin.log.ILogWriter;
import i2f.core.zplugin.log.data.LogData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/29 9:08
 * @desc
 */
public class BroadcastLogWriter implements ILogWriter {
    public static final String LOG_NAME_STDOUT_WRITER="STDOUT";
    protected ConcurrentHashMap<String,ILogWriter> logWriters=new ConcurrentHashMap<>();
    public BroadcastLogWriter(){
        registerLogWriter(LOG_NAME_STDOUT_WRITER,new StdoutLogWriter());
    }
    public BroadcastLogWriter(boolean registryStdout){
        if(registryStdout){
            registerLogWriter(LOG_NAME_STDOUT_WRITER,new StdoutLogWriter());
        }
    }
    public void registerLogWriter(Class<? extends ILogWriter> clazz){
        String name=clazz.getSimpleName();
        name=name.substring(0,1).toLowerCase()+name.substring(1);
        registerLogWriter(name,clazz);
    }
    public void registerLogWriter(String name,Class<? extends ILogWriter> clazz){
        ILogWriter writer= ReflectResolver.instance(clazz);
        registerLogWriter(name,writer);
    }
    public void registerLogWriter(String name,ILogWriter writer){
        logWriters.put(name, new DecisionLogWriterAdapter(writer));
    }
    public ILogWriter removeLogWriter(String name){
        return logWriters.remove(name);
    }

    @Override
    public void write(LogData data) {
        for(ILogWriter item : logWriters.values()){
            item.write(data);
        }
    }
}
