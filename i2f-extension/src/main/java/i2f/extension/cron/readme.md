# 单应用嵌入式 CRON 任务
---

## 使用
- 导入maven依赖
```xml
<dependency>
	<groupId>com.cronutils</groupId>
	<artifactId>cron-utils</artifactId>
	<version>9.1.5</version>
</dependency>
```
- 添加本包到自己的项目
    - CronUtil
    - CronExecutor
- 开始使用
```java
CronExecutor executor = new CronExecutor();

executor.submit(CronType.QUARTZ, "0/3 * * * * ?", () -> {
    try{
        Thread.sleep(rand.nextInt(3000));
    }catch(Exception e){
    
    }
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());
    System.out.println("exec:" + fmt.format(date));
});
```
- 这个示例中，便实现了没3秒钟执行一次输出任务
- 其中用到了NamingThreadFactory
- 这是一个可以具名的线程池工厂
- 代码如下
```java
package i2f.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/8/5 16:31
 * @desc
 */
public class NamingThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public NamingThreadFactory() {
        this(null, null, null);
    }

    public NamingThreadFactory(String poolPrefix, String threadPrefix) {
        this(null, poolPrefix, threadPrefix);
    }

    public NamingThreadFactory(ThreadGroup group, String poolPrefix, String threadPrefix) {
        if (group == null) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
        }

        String poolName = "pool";
        String threadName = "thread";
        if (poolPrefix != null) {
            poolName = poolPrefix;
        }
        if (threadPrefix != null) {
            threadName = threadPrefix;
        }

        this.group = group;
        this.namePrefix = poolName + "-" +
                poolNumber.getAndIncrement() +
                "-" + threadName + "-";

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}

```