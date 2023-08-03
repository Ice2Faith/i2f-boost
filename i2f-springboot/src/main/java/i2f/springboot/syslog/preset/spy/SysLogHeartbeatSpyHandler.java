package i2f.springboot.syslog.preset.spy;

import i2f.core.os.perf.PerfUtil;
import i2f.core.os.perf.data.LinuxFreeDto;
import i2f.core.os.perf.data.LinuxTop5Dto;
import i2f.springboot.syslog.SysLogger;
import i2f.springboot.syslog.SysLoggerFactory;
import i2f.springboot.syslog.annotations.SysLog;
import i2f.springboot.syslog.consts.LogKeys;
import i2f.springboot.syslog.consts.LogType;
import i2f.springboot.syslog.preset.JvmUtil;
import i2f.springboot.syslog.preset.lifecycle.SysLogApplicationRunnerListener;
import i2f.springboot.syslog.util.SysLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.management.MemoryUsage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/2/8 15:49
 * @desc
 */
@Slf4j
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.heartbeat.enable:false}")
@Component
public class SysLogHeartbeatSpyHandler implements InitializingBean {

    private SysLogger logger = SysLoggerFactory.getLogger(SysLogHeartbeatSpyHandler.class);

    @Value("${sys.log.heartbeat.rate-mill:300000}")
    private long heartbeatMillSecond = 5 * 60 * 1000;

    private static ScheduledExecutorService schedulePool = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void afterPropertiesSet() throws Exception {
        initHeartbeatTask();
    }

    private void initHeartbeatTask() {
        schedulePool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    heartBeatRun();
                } catch (Throwable e) {
                    log.warn("系统日志心跳日志异常：" + e.getMessage(), e);
                }
            }
        }, 0, heartbeatMillSecond, TimeUnit.MILLISECONDS);

    }

    private double getJvmUseRate() {
        Runtime runtime = Runtime.getRuntime();
        return (((int) ((1.0 - (runtime.freeMemory() * 1.0 / runtime.totalMemory())) * 10000)) / 100.0);
    }


    private void heartBeatRun() throws Throwable {
        long runTime = System.currentTimeMillis() - SysLogApplicationRunnerListener.BOOT_TIME_MS;
        String traceId = SysLogUtil.makeTraceId();

        logJvmMemory(traceId, runTime);

        logJvmStatus(traceId, runTime);

        logSysFree(traceId, runTime);

        logSysTop(traceId, runTime);
    }

    private void logJvmMemory(String traceId, long runTime) {
        logger.info(LogKeys.SYS_JVM_MEMORY.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_MEMORY.key());
            dto.setLogVal(String.format("%.2f", getJvmUseRate()));
            Runtime runtime = Runtime.getRuntime();
            long freeMemory = runtime.freeMemory();
            long totalMemory = runtime.totalMemory();
            long maxMemory = runtime.maxMemory();

            StringBuilder builder = new StringBuilder();
            builder.append("useRate:" + (((int) ((1.0 - (freeMemory * 1.0 / totalMemory)) * 10000)) / 100.0)).append("% | ")
                    .append("free:").append(freeMemory / 1024 / 1024).append("M").append(" | ")
                    .append("total:").append(totalMemory / 1024 / 1024).append("M").append(" | ")
                    .append("max:").append(maxMemory / 1024 / 1024).append("M");
            dto.setLogContent(builder.toString());
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
    }

    private void logJvmStatus(String traceId, long runTime) {
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".jvm.up.millsec");
            long cnt = JvmUtil.getVmUpMilliSeconds();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("Vm在线时间：%ds", cnt / 1000));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        MemoryUsage heapMem = JvmUtil.getHeapMemoryUsage();
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.init");
            long cnt = heapMem.getInit();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("初始化堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.used");
            long cnt = heapMem.getUsed();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("已使用堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.committed");
            long cnt = heapMem.getCommitted();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("已提交堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.max");
            long cnt = heapMem.getMax();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("最大堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.init.rate");
            double rate = heapMem.getInit() * 1.0 / heapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("初始化堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.used.rate");
            double rate = heapMem.getUsed() * 1.0 / heapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("已使用堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".heap.committed.rate");
            double rate = heapMem.getCommitted() * 1.0 / heapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("已提交堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });


        MemoryUsage nonHeapMem = JvmUtil.getNonHeapMemoryUsage();
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.init");
            long cnt = nonHeapMem.getInit();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("初始化非堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.used");
            long cnt = nonHeapMem.getUsed();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("已使用非堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.committed");
            long cnt = nonHeapMem.getCommitted();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("已提交非堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.max");
            long cnt = nonHeapMem.getMax();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("最大非堆大小：%.2fMB", cnt * 1.0 / 1024 / 1024));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.init.rate");
            double rate = nonHeapMem.getInit() * 1.0 / nonHeapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("初始化非堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.used.rate");
            double rate = nonHeapMem.getUsed() * 1.0 / nonHeapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("已使用非堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".non-heap.committed.rate");
            double rate = nonHeapMem.getCommitted() * 1.0 / nonHeapMem.getMax() * 100;
            dto.setLogVal(String.format("%.2f", rate));

            dto.setLogContent(String.format("已提交非堆比例：%.2f%%", rate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });


        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".pending.finalization.count");
            int cnt = JvmUtil.getObjectPendingFinalizationCount();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("等待结束回收：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".thread.count");
            int cnt = JvmUtil.getThreadCount();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("线程数：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".thread.total.started.count");
            long cnt = JvmUtil.getTotalStartedThreadCount();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("总已启动线程数：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".thread.peek.count");
            int cnt = JvmUtil.getPeakThreadCount();
            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("活动线程数峰值：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
    }


    private void logSysFree(String traceId, long runTime) {
        LinuxFreeDto free = PerfUtil.getLinuxFree();
        if (free == null) {
            return;
        }
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.total");
            dto.setLogVal(String.valueOf(free.memTotal));
            dto.setLogContent(String.format("内存总量：%.2fMB", (free.memTotal / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.used");
            dto.setLogVal(String.valueOf(free.memUsed));
            dto.setLogContent(String.format("内存已使用：%.2fMB", (free.memUsed / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.free");
            dto.setLogVal(String.valueOf(free.memUsed));
            dto.setLogContent(String.format("内存剩余：%.2fMB", (free.memFree / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.shared");
            dto.setLogVal(String.valueOf(free.memUsed));
            dto.setLogContent(String.format("内存共享：%.2fMB", (free.memShared / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.buffCache");
            dto.setLogVal(String.valueOf(free.memBuffCache));
            dto.setLogContent(String.format("内存缓存：%.2fMB", (free.memBuffCache / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".mem.available");
            dto.setLogVal(String.valueOf(free.memAvailable));
            dto.setLogContent(String.format("内存已使用：%.2fMB", (free.memAvailable / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".swap.total");
            dto.setLogVal(String.valueOf(free.swapTotal));
            dto.setLogContent(String.format("虚拟内存总共：%.2fMB", (free.swapTotal / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".swap.used");
            dto.setLogVal(String.valueOf(free.swapUsed));
            dto.setLogContent(String.format("虚拟内存已使用：%.2fMB", (free.swapUsed / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_FREE.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_FREE.key() + ".swap.free");
            dto.setLogVal(String.valueOf(free.swapFree));
            dto.setLogContent(String.format("虚拟内存剩余：%.2fMB", (free.swapFree / 1024.0)));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
    }

    private void logSysTop(String traceId, long runTime) {
        LinuxTop5Dto top5 = PerfUtil.getLinuxTop5();
        if (top5 == null) {
            return;
        }
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.date");
            dto.setLogVal(String.valueOf(top5.topDate));
            dto.setLogContent(String.format("系统时间：%s", top5.topDate));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.days");
            dto.setLogVal(String.valueOf(top5.topDays));
            dto.setLogContent(String.format("已运行天数：%d", top5.topDays));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.time");
            dto.setLogVal(String.valueOf(top5.topTime));
            dto.setLogContent(String.format("已运行时间：%s", top5.topTime));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.users");
            dto.setLogVal(String.valueOf(top5.topUsers));
            dto.setLogContent(String.format("在线用户数：%d", top5.topUsers));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.load.avg.1");
            dto.setLogVal(String.valueOf(top5.loadAverage1));
            dto.setLogContent(String.format("系统1分钟负载：%.2f", top5.loadAverage1));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.load.avg.2");
            dto.setLogVal(String.valueOf(top5.loadAverage2));
            dto.setLogContent(String.format("系统5分钟负载：%.2f", top5.loadAverage2));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".top.load.avg.3");
            dto.setLogVal(String.valueOf(top5.loadAverage3));
            dto.setLogContent(String.format("系统15分钟负载：%.2f", top5.loadAverage3));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".tasks.total");
            dto.setLogVal(String.valueOf(top5.tasksTotal));
            dto.setLogContent(String.format("任务总数：%d", top5.tasksTotal));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".tasks.running");
            dto.setLogVal(String.valueOf(top5.tasksRunning));
            dto.setLogContent(String.format("任务运行中：%d", top5.tasksRunning));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".tasks.sleeping");
            dto.setLogVal(String.valueOf(top5.tasksSleeping));
            dto.setLogContent(String.format("任务睡眠中：%d", top5.tasksSleeping));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".tasks.stopped");
            dto.setLogVal(String.valueOf(top5.tasksStopped));
            dto.setLogContent(String.format("任务已停止：%d", top5.tasksStopped));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".tasks.zombie");
            dto.setLogVal(String.valueOf(top5.tasksZombie));
            dto.setLogContent(String.format("任务僵尸：%d", top5.tasksZombie));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.us");
            dto.setLogVal(String.valueOf(top5.cpuUs));
            dto.setLogContent(String.format("CPU用户空间占比：%.2f", top5.cpuUs));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.sy");
            dto.setLogVal(String.valueOf(top5.cpuSy));
            dto.setLogContent(String.format("CPU内核空间占比：%.2f", top5.cpuSy));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.ni");
            dto.setLogVal(String.valueOf(top5.cpuNi));
            dto.setLogContent(String.format("CPU用户空间改变优先级占比：%.2f", top5.cpuNi));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.id");
            dto.setLogVal(String.valueOf(top5.cpuId));
            dto.setLogContent(String.format("CPU空闲占比：%.2f", top5.cpuId));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.wa");
            dto.setLogVal(String.valueOf(top5.cpuWa));
            dto.setLogContent(String.format("CPU等待输入输出占比：%.2f", top5.cpuWa));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.hi");
            dto.setLogVal(String.valueOf(top5.cpuHi));
            dto.setLogContent(String.format("CPU硬中断占比：%.2f", top5.cpuHi));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.si");
            dto.setLogVal(String.valueOf(top5.cpuSi));
            dto.setLogContent(String.format("CPU软中断占比：%.2f", top5.cpuSi));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".cpu.st");
            dto.setLogVal(String.valueOf(top5.cpuSt));
            dto.setLogContent(String.format("CPU虚拟等待实际CPU占比：%.2f", top5.cpuSt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".mem.total");
            dto.setLogVal(String.valueOf(top5.kibMemTotal));
            dto.setLogContent(String.format("内存总量：%dKB", top5.kibMemTotal));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".mem.free");
            dto.setLogVal(String.valueOf(top5.kibMemFree));
            dto.setLogContent(String.format("内存剩余：%dKB", top5.kibMemFree));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".mem.used");
            dto.setLogVal(String.valueOf(top5.kibMemUsed));
            dto.setLogContent(String.format("内存已使用：%dKB", top5.kibMemUsed));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".mem.buffCache");
            dto.setLogVal(String.valueOf(top5.kibMemBuffCache));
            dto.setLogContent(String.format("内存缓存：%dKB", top5.kibMemBuffCache));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".swap.total");
            dto.setLogVal(String.valueOf(top5.kibSwapTotal));
            dto.setLogContent(String.format("虚拟内存总量：%dKB", top5.kibSwapTotal));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".swap.free");
            dto.setLogVal(String.valueOf(top5.kibSwapFree));
            dto.setLogContent(String.format("虚拟内存剩余：%dKB", top5.kibSwapFree));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".swap.used");
            dto.setLogVal(String.valueOf(top5.kibSwapUsed));
            dto.setLogContent(String.format("虚拟内存已使用：%dKB", top5.kibSwapUsed));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
        logger.info(LogKeys.SYS_CMD_TOP.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_CMD_TOP.key() + ".swap.availMem");
            dto.setLogVal(String.valueOf(top5.kibSwapAvailMem));
            dto.setLogContent(String.format("虚拟内存可用：%dKB", top5.kibSwapAvailMem));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
    }
}
