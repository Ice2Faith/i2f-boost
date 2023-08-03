package i2f.springboot.syslog.preset.lifecycle;

import i2f.springboot.syslog.SysLogger;
import i2f.springboot.syslog.SysLoggerFactory;
import i2f.springboot.syslog.annotations.SysLog;
import i2f.springboot.syslog.consts.LogKeys;
import i2f.springboot.syslog.consts.LogType;
import i2f.springboot.syslog.core.SpringContextProvider;
import i2f.springboot.syslog.preset.JvmUtil;
import i2f.springboot.syslog.util.SysLogUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/7 18:06
 * @desc
 */
@SysLog(value = false)
@ConditionalOnExpression("${sys.log.listener.enable:false}")
@Component
public class SysLogApplicationRunnerListener implements ApplicationRunner {
    private static SysLogger logger = SysLoggerFactory.getLogger(SysLogApplicationRunnerListener.class);
    public static final long BOOT_TIME_MS = System.currentTimeMillis();
    public static long INITIALED_TIME_MS = BOOT_TIME_MS - 1L;

    static {
        logger.info(LogKeys.APP_STARTUP.desc(), (dto) -> {
            String time = SysLogUtil.timeFmt.get().format(new Date(BOOT_TIME_MS));

            dto.setLogType(LogType.STATUS.code());

            SysLogUtil.fillLogContentByArgs(dto, "系统启动，time=" + time);

            dto.setLogKey(LogKeys.APP_STARTUP.key());
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        INITIALED_TIME_MS = System.currentTimeMillis();
        long bootTime = INITIALED_TIME_MS - BOOT_TIME_MS;

        logger.info(LogKeys.APP_INITIALED.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());

            SysLogUtil.fillLogContentByArgs(dto, "初始化完成，port=" + SpringContextProvider.getEnv("server.port")
                            + ",active=" + SpringContextProvider.getEnv("spring.profiles.active"),
                    args.getSourceArgs());

            dto.setLogKey(LogKeys.APP_INITIALED.key());
            dto.setCostTime(bootTime);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });

        long runTime = System.currentTimeMillis();
        String traceId = SysLogUtil.makeTraceId();
        logJvmStartInfo(traceId, runTime);
    }

    private void logJvmStartInfo(String traceId, long runTime) {

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".jvm.start.argument");
            String[] args = JvmUtil.getVmArguments();
            dto.setLogVal(null);

            SysLogUtil.fillLogContentByArgs(dto, args);
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });


        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".jvm.start.date");
            Date date = JvmUtil.getVmStartDate();
            String dateFmt = SysLogUtil.timeFmt.get().format(date);

            dto.setLogVal(String.format("%s", dateFmt));

            dto.setLogContent(String.format("Vm启动时间：%s", dateFmt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".class.loaded.count");
            int cnt = JvmUtil.getLoadedClassCount();

            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("已加载类数量：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".class.total.loaded.count");
            long cnt = JvmUtil.getTotalLoadedClassCount();

            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("总共已加载类数量：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".class.unloaded.loaded.count");
            long cnt = JvmUtil.getUnloadedClassCount();

            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("未加载类数量：%d", cnt));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });

        logger.info(LogKeys.SYS_JVM_STATUS.desc(), (dto) -> {
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.SYS_JVM_STATUS.key() + ".compile.total.millsec");
            long cnt = JvmUtil.getTotalCompilationMilliSeconds();

            dto.setLogVal(String.format("%d", cnt));

            dto.setLogContent(String.format("总共编译时长：%.2f", cnt / 1000.0));
            dto.setCostTime(runTime);
            dto.setTraceLevel(0);
            dto.setTraceId(traceId);
        });
    }

    @EventListener
    public void onStart(ContextStartedEvent event) {
        long useTime = System.currentTimeMillis() - BOOT_TIME_MS;
        logger.info(LogKeys.APP_CONTEXT_STARTED.desc(), (dto) -> {
            String time = SysLogUtil.timeFmt.get().format(new Date(event.getTimestamp()));
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.APP_CONTEXT_STARTED.key());
            dto.setLogVal(time);
            dto.setCostTime(useTime);
            SysLogUtil.fillLogContentByArgs(dto, "上下文启动，time=" + time);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });
    }

    @EventListener
    public void onStop(ContextStoppedEvent event) {
        long useTime = System.currentTimeMillis() - BOOT_TIME_MS;
        logger.info(LogKeys.APP_CONTEXT_STOPPED.desc(), (dto) -> {
            String time = SysLogUtil.timeFmt.get().format(new Date(event.getTimestamp()));
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.APP_CONTEXT_STOPPED.key());
            dto.setLogVal(time);
            dto.setCostTime(useTime);
            SysLogUtil.fillLogContentByArgs(dto, "上下文停止，time=" + time);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });
    }

    @EventListener
    public void onStop(ContextRefreshedEvent event) {
        long useTime = System.currentTimeMillis() - BOOT_TIME_MS;
        logger.info(LogKeys.APP_CONTEXT_REFRESH.desc(), (dto) -> {
            String time = SysLogUtil.timeFmt.get().format(new Date(event.getTimestamp()));
            dto.setLogType(LogType.STATUS.code());
            dto.setLogKey(LogKeys.APP_CONTEXT_REFRESH.key());
            dto.setLogVal(time);
            dto.setCostTime(useTime);
            SysLogUtil.fillLogContentByArgs(dto, "上下文刷新，time=" + time);
            SysLogUtil.fillSystemModuleByEnvironment(dto);
        });
    }
}
