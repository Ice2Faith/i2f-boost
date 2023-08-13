package i2f.core.log.core;

import i2f.core.log.data.LogDto;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/8/2 9:04
 * @desc
 */
public class LogDispatcher  {
    private static AtomicBoolean shutdown=new AtomicBoolean(false);
    static{
        Thread logDispatchWriterThread = new Thread(() -> {
            while(!LogContext.initialed.get()){
                try{
                    Thread.sleep(30);
                }catch(Exception e){
                    System.err.println("LogDispatcher wait initialed sleep error: "+e.getMessage());
                    e.printStackTrace();
                }
            }
            while(true){
                try{
                    LogDto dto = LogContext.queue.take();
                    if(shutdown.get()){
                        break;
                    }
                    for (ILogWriter writer : LogContext.writers.values()) {
                        try{
                            writer.write(dto);
                        }catch(Exception e){
                            System.err.println("LogDispatcher to writer "+writer.getClass().getName()+" error: "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }catch(Exception e){
                    System.err.println("LogDispatcher take LogDto error: "+e.getMessage());
                    e.printStackTrace();
                }
            }

        }, "log-dispatcher");

        logDispatchWriterThread.start();

        ThreadExitMonitor mainExitMonitor=new ThreadExitMonitor("log-main-exit-monitor",(info)->{
            return "main".equals(info.getThreadName());
        },()->{
            if(LogContext.canShutdown.get()){
                shutdown.set(true);
                write(LogCore.newLog());
                shutdown.set(true);
                write(LogCore.newLog());
            }
        });

        mainExitMonitor.start();
    }

    public static void write(LogDto dto){
        LogContext.queue.add(dto);
        int max = LogContext.maxQueue.get();
        if(max>0){
            int size = LogContext.queue.size();
            while(size>max){
                try{
                    LogDto elem = LogContext.queue.take();
                    System.err.println("LogDispatcher drop oversize LogDto : "+elem.getContent());
                }catch(Exception e){
                    System.err.println("LogDispatcher take oversize LogDto error: "+e.getMessage());
                    e.printStackTrace();
                }
                size--;
            }
        }
    }

}
