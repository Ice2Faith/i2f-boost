package i2f.clock;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/3/25 11:31
 * @desc 由于 System.currentTimeMillis() 是一个native调用
 * 依赖于操作系统提供的时间戳，涉及到系统调用
 * 而且部分系统，需要转入内核态进行获取
 * 导致了在高并发场景中，由于native调用的上下文切换
 * 会产生性能问题
 * 因此，采用Schedule定时缓存时间戳
 * 其实也就是做了一层cache代理
 * 因为是一层代理，所以也就不会出现时间回播问题
 * 可以用于分布式ID的生成中，例如UUID，雪花ID等的生成
 * 以下是一些对比：
 * 循环获取 1,000,000,000 次，进行 100 次测试
 * --------------------
 * clock :TimeCounter{diff=  19, avg=  28.80, max=  41, min=  19, sum=   144, count=  5}
 * system:TimeCounter{diff= 980, avg=2550.20, max=6134, min=   7, sum= 12751, count=  5}
 * rate  :(this:other)={diff=   1.94%, avg=   1.13%, max=   0.67%, min= 271.43%, sum=   1.13%, count= 100.00%}
 * acc   :(this:other)={diff=5157.89%, avg=8854.86%, max=14960.98%, min=  36.84%, sum=8854.86%, count= 100.00%}
 * --------------------
 * clock :TimeCounter{diff=  22, avg= 198.70, max=4421, min=   0, sum= 19870, count=100}
 * system:TimeCounter{diff=3997, avg=3566.89, max=6134, min=   7, sum=356689, count=100}
 * rate  :(this:other)={diff=   0.55%, avg=   5.57%, max=  72.07%, min=   0.00%, sum=   5.57%, count= 100.00%}
 * acc   :(this:other)={diff=18168.18%, avg=1795.11%, max= 138.75%, min=Infinity%, sum=1795.11%, count= 100.00%}
 * 可以得到，
 * 长期来看：加速比能够达到1795%，也就是17倍
 * 短期来看，加速比能够达到8854%,也就是88倍
 * 接下来，循环 10,000,000 次，进行 10,000 次测试
 * --------------------
 * clock :TimeCounter{diff=   0, avg=   0.40, max= 127, min=   0, sum=  3982, count=10000}
 * system:TimeCounter{diff=  56, avg=  59.62, max=1609, min=   0, sum=596200, count=10000}
 * rate  :(this:other)={diff=   0.00%, avg=   0.67%, max=   7.89%, min=    NaN%, sum=   0.67%, count= 100.00%}
 * acc   :(this:other)={diff=Infinity%, avg=14972.38%, max=1266.93%, min=    NaN%, sum=14972.38%, count= 100.00%}
 * 可以看到，在此测试中，加速比达到14972%，也就是149倍
 * 效果是非常明显的
 * 在实际对比中，此工具获取的时间平均晚于默认时间3ms左右
 * 峰值为400ms
 */
public class SystemClock {
    public static final ThreadGroup THREAD_GROUP=new ThreadGroup("system");
    public static final String THREAD_NAME="system.clock";

    protected static volatile long ts=System.currentTimeMillis();
    static {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1,
                (task)->{
                    Thread thread = new Thread(THREAD_GROUP, task, THREAD_NAME);
                    thread.setDaemon(true);
                    return thread;
                }
        );
        ts=System.currentTimeMillis();
        pool.scheduleAtFixedRate(()->{
            ts=System.currentTimeMillis();
        },0,1, TimeUnit.MILLISECONDS);
    }

    public static long currentTimeMillis(){
        return ts;
    }
}
