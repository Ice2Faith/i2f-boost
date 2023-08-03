package i2f.core.log.core;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2023/8/2 18:53
 * @desc
 */
public class ThreadExitMonitor {
    private String name;
    private Predicate<ThreadInfo> predicate;
    private Runnable callback;
    private AtomicBoolean started=new AtomicBoolean(false);

    public ThreadExitMonitor(String name, Predicate<ThreadInfo> predicate, Runnable callback) {
        this.name = name;
        this.predicate = predicate;
        this.callback = callback;
    }

    public void start(){
        if(started.getAndSet(true)){
            return;
        }
        Thread mainExitMonitorThread=new Thread(()->{
            while(true){
                boolean exit=true;
                ThreadMXBean tbean = ManagementFactory.getThreadMXBean();
                long[] tids = tbean.getAllThreadIds();
                for (long tid : tids) {
                    ThreadInfo info = tbean.getThreadInfo(tid);
                    if(info!=null){
                        if(predicate.test(info)){
                            exit=false;
                        }
                    }
                }
                if(exit){
                    callback.run();
                    break;
                }
                try{
                    Thread.sleep(300);
                }catch(Exception e){

                }
            }
        },name);
        mainExitMonitorThread.start();
    }
}
