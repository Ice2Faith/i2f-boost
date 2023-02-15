package i2f.springboot.log.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogKeys;
import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.consts.LogOperateType;
import i2f.springboot.log.consts.LogType;
import i2f.springboot.log.core.SpringContextProvider;
import i2f.springboot.log.data.*;
import i2f.springboot.log.handler.impl.Slf4jSysLogHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/2/7 15:05
 * @desc
 */
@Slf4j
public class SysLogUtil {

    public static final String REQUEST_TRACE_ID_KEY = "req_trace_id";
    public static final String REQUEST_TRACE_LEVEL_KEY = "req_trace_level";

    public static volatile InheritableThreadLocal<String> traceIdLocal = new InheritableThreadLocal<>();
    public static volatile InheritableThreadLocal<Integer> traceLevelLocal = new InheritableThreadLocal<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static ConcurrentHashMap<Method, Optional<SysLog>> fastMethodAnnMap = new ConcurrentHashMap<>();

    public static SysLog getMethodSysLog(Method method) {
        if (fastMethodAnnMap.containsKey(method)) {
            return fastMethodAnnMap.get(method).orElseGet(() -> null);
        }
        SysLog ann = getAnnotationMethod(method, SysLog.class);
        fastMethodAnnMap.put(method, Optional.ofNullable(ann));
        return ann;
    }

    public static SysLogDto instanceLog() {
        SysLogDto ret = new SysLogDto();
        ret.setCreateTime(new Date(System.currentTimeMillis()));
        ret.setLogType(LogType.OUTPUT.code());
        ret.setLogLevel(LogLevel.INFO.code());
        ret.setOperateType(LogOperateType.QUERY.code());
        return ret;
    }

    public static SysLogDto instanceLog(Method method) {
        SysLogDto ret = instanceLog();

        ret.setLogLocation(method.getDeclaringClass().getName());
        ret.setLogType(LogType.API.code());
        ret.setLogLevel(LogLevel.INFO.code());
        ret.setOperateType(LogOperateType.QUERY.code());
        ret.setJavaMethod(method.toGenericString());

        SysLog ann = getMethodSysLog(method);
        if (ann != null) {
            if (!StringUtils.isEmpty(ann.system())) {
                ret.setSrcSystem(ann.system());
            }
            if (!StringUtils.isEmpty(ann.module())) {
                ret.setSrcModule(ann.module());
            }
            if (!StringUtils.isEmpty(ann.label())) {
                ret.setSrcLabel(ann.label());
            }
            ret.setOperateType(ann.operate().code());
            ret.setLogLevel(ann.level().code());
            ret.setLogType(ann.type().code());
        }

        return ret;
    }

    public static void fillLogKeyValBySpringMvc(SysLogDto dto, Method method) {
        if (!hasLoadSpringMvc()) {
            return;
        }
        try {
            List<String> mappings = null;
            if (mappings == null) {
                RequestMapping mann = getAnnotation(method, RequestMapping.class);
                if (mann != null) {
                    mappings = new ArrayList<>();
                    for (String item : mann.value()) {
                        mappings.add(item);
                    }
                }
            }
            if (mappings == null) {
                GetMapping mann = getAnnotation(method, GetMapping.class);
                if (mann != null) {
                    mappings = new ArrayList<>();
                    for (String item : mann.value()) {
                        mappings.add(item);
                    }
                }
            }
            if (mappings == null) {
                PostMapping mann = getAnnotation(method, PostMapping.class);
                if (mann != null) {
                    mappings = new ArrayList<>();
                    for (String item : mann.value()) {
                        mappings.add(item);
                    }
                }
            }
            if (mappings == null) {
                PutMapping mann = getAnnotation(method, PutMapping.class);
                if (mann != null) {
                    mappings = new ArrayList<>();
                    for (String item : mann.value()) {
                        mappings.add(item);
                    }
                }
            }
            if (mappings == null) {
                DeleteMapping mann = getAnnotation(method, DeleteMapping.class);
                if (mann != null) {
                    mappings = new ArrayList<>();
                    for (String item : mann.value()) {
                        mappings.add(item);
                    }
                }
            }

            RequestMapping cann = getAnnotation(method.getDeclaringClass(), RequestMapping.class);

            List<String> basePaths = null;
            if (cann != null) {
                basePaths = new ArrayList<>();
                for (String item : cann.value()) {
                    basePaths.add(item);
                }
            }

            List<String> result = null;
            if (mappings != null) {
                result = new ArrayList<>();
                for (String mapping : mappings) {
                    if (basePaths != null) {
                        for (String basePath : basePaths) {
                            String path = combineSep("/", basePath, mapping);
                            result.add(path);
                        }
                    } else {
                        result.add(mapping);
                    }
                }
            } else {
                if (basePaths != null) {
                    result = new ArrayList<>();
                    result.addAll(basePaths);
                }
            }

            if (result != null) {
                dto.setLogKey(LogKeys.API_NAME.key());
                dto.setLogVal(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            }
        } catch (Exception e) {
            log.debug("填充系统日志LogKV信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public static String combineSep(String sep, String prefix, String suffix) {
        if (prefix == null) {
            return suffix;
        }
        if (suffix == null) {
            return prefix;
        }
        if (sep == null) {
            return prefix + suffix;
        }
        if (prefix.endsWith(sep)) {
            if (suffix.startsWith(sep)) {
                return prefix + suffix.substring(sep.length());
            } else {
                return prefix + suffix;
            }
        } else {
            if (suffix.startsWith(sep)) {
                return prefix + suffix;
            } else {
                return prefix + sep + suffix;
            }
        }
    }

    public static boolean hasLoadSwagger() {
        String className = "io.swagger.annotations.Api";
        return hasLoadClass(className);
    }

    public static void fillLabelBySwagger(SysLogDto dto, Method method) {
        if (!hasLoadSwagger()) {
            return;
        }
        try {
            ApiOperation mann = getAnnotation(method, ApiOperation.class);
            StringBuilder builder = new StringBuilder();
            if (mann != null) {
                String[] tags = mann.tags();
                String notes = mann.notes();
                String oper = mann.value();
                for (String tag : tags) {
                    if (!StringUtils.isEmpty(tag)) {
                        builder.append("[").append(tag).append("]");
                    }
                }
                if (!StringUtils.isEmpty(oper)) {
                    builder.append(" / ").append(oper);
                }
                if (!StringUtils.isEmpty(notes)) {
                    builder.append(" / ").append(notes);
                }
            }
            String label = builder.toString();
            if (!StringUtils.isEmpty(label)) {
                dto.setSrcLabel(label);
            }

            Api cann = getAnnotation(method.getDeclaringClass(), Api.class);
            if (cann != null) {
                builder.setLength(0);
                String[] tags = cann.tags();
                String description = cann.description();
                String oper = cann.value();
                for (String tag : tags) {
                    if (!StringUtils.isEmpty(tag)) {
                        builder.append("[").append(tag).append("]");
                    }
                }
                if (!StringUtils.isEmpty(oper)) {
                    builder.append(" / ").append(oper);
                }
                if (!StringUtils.isEmpty(description)) {
                    builder.append(" / ").append(description);
                }
            }
            String module = builder.toString();
            if (!StringUtils.isEmpty(module)) {
                dto.setSrcModule(module);
            }
        } catch (Throwable e) {
            log.debug("填充系统日志Swagger信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public static void fillSystemModuleByEnvironment(SysLogDto dto) {
        if (StringUtils.isEmpty(dto.getSrcSystem())) {
            dto.setSrcSystem(SpringContextProvider.getEnv("sys.log.system"));
        }
        if (StringUtils.isEmpty(dto.getSrcSystem())) {
            dto.setSrcSystem(SpringContextProvider.getEnv("spring.application.name"));
        }
        if (StringUtils.isEmpty(dto.getSrcModule())) {
            dto.setSrcModule(SpringContextProvider.getEnv("sys.log.module"));
        }
        if (StringUtils.isEmpty(dto.getSrcModule())) {
            dto.setSrcModule(SpringContextProvider.getEnv("spring.profiles.active"));
        }
    }

    public static boolean hasLoadSpringMvc() {
        String className = "org.springframework.web.SpringServletContainerInitializer";
        return hasLoadClass(className);
    }

    public static boolean hasLoadClass(String className) {
        if (className == null) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return true;
            }
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (clazz != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static void prepareCurrentTrace() {
        if (!hasLoadSpringMvc()) {
            return;
        }
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes());
            HttpServletRequest request = attrs.getRequest();
            String traceId = (String) request.getAttribute(REQUEST_TRACE_ID_KEY);
            Integer traceLevel = (Integer) request.getAttribute(REQUEST_TRACE_LEVEL_KEY);

            if (traceId == null) {
                traceId = makeTraceId();
                traceLevel = 0;
                request.setAttribute(REQUEST_TRACE_ID_KEY, traceId);
                request.setAttribute(REQUEST_TRACE_LEVEL_KEY, traceLevel);
            }
            if (traceId != null) {
                traceIdLocal.set(traceId);
                traceLevelLocal.set(traceLevel);
            }

        } catch (Exception e) {
            log.debug("填充系统日志请求信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public static void addTraceLevel() {
        Integer traceLevel = traceLevelLocal.get();
        if (traceLevel == null) {
            traceLevel = 0;
        } else {
            traceLevel = traceLevel + 1;
        }
        traceLevelLocal.set(traceLevel);
    }

    public static void subTraceLevel() {
        Integer traceLevel = traceLevelLocal.get();
        if (traceLevel == null) {
            traceLevel = 0;
        } else {
            traceLevel = traceLevel - 1;
        }
        traceLevelLocal.set(traceLevel);
    }

    public static void fillRequestInfosByRequestContextHolder(SysLogDto dto) {
        if (!hasLoadSpringMvc()) {
            return;
        }
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes());
            HttpServletRequest request = attrs.getRequest();

            dto.setClientIp(getIp(request));

            dto.setRequestUrl(request.getRequestURI());
            dto.setRequestParam(request.getQueryString());
            dto.setRequestType(request.getMethod());


        } catch (Exception e) {
            log.debug("填充系统日志请求信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public static void fillLogExceptionTrackByException(SysLogDto dto, Throwable ex) {
        if (ex != null) {
            try {
                dto.setExceptClass(ex.getClass().getName());
                dto.setExceptMsg(ex.getMessage());
                if (ex instanceof SQLException) {
                    dto.setExceptType(4);
                } else if (ex instanceof RuntimeException) {
                    dto.setExceptType(1);
                } else if (ex instanceof Exception) {
                    dto.setExceptType(0);
                } else if (ex instanceof Error) {
                    dto.setExceptType(2);
                } else {
                    dto.setExceptType(3);
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(bos);
                ex.printStackTrace(writer);
                writer.flush();
                writer.close();
                byte[] bytes = bos.toByteArray();
                String exceptionStack = new String(bytes);
                dto.setExceptStack(exceptionStack);
            } catch (Exception e) {
                log.debug("填充系统异常信息失败：" + e.getClass().getName() + ":" + e.getMessage());
            }
        }
    }

    public static void fillLogContentByArgs(SysLogDto dto, Object... args) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(args);
            dto.setLogContent(json);
        } catch (Exception e) {
            log.debug("填充系统日志参数信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public static String makeTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void fillTraceId(SysLogDto dto) {
        String traceId = traceIdLocal.get();
        Integer traceLevel = traceLevelLocal.get();
        if (traceId == null) {
            traceId = makeTraceId();
            traceLevel = 0;
            traceIdLocal.set(traceId);
            traceLevelLocal.set(traceLevel);
        } else {
            traceLevelLocal.set(traceLevel);
        }
        dto.setTraceId(traceId);
        dto.setTraceLevel(traceLevel);
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ann = elem.getDeclaredAnnotation(clazz);
        if (ann == null) {
            ann = elem.getAnnotation(clazz);
        }
        return ann;
    }

    public static <T extends Annotation> T getAnnotationMethod(Method method, Class<T> clazz) {
        T ann = getAnnotation(method, clazz);
        if (ann == null) {
            ann = getAnnotation(method.getDeclaringClass(), clazz);
        }
        return ann;
    }

    //////////////////////////////////////////////////////////
    private static Map<String, Logger> cachedSlf4jLogger = new ConcurrentHashMap<>();

    public static Logger getSlf4jLogger(String location) {
        if (location == null) {
            location = Slf4jSysLogHandler.class.getName();
        }
        if (cachedSlf4jLogger.containsKey(location)) {
            return cachedSlf4jLogger.get(location);
        }
        Logger logger = LoggerFactory.getLogger(location);
        cachedSlf4jLogger.put(location, logger);
        return logger;
    }


    //////////////////////////////////////////////////////////

    public static ThreadLocal<SimpleDateFormat> timeFmt = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    });

    public static String log2str(SysLogDto dto) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ")
                .append(timeFmt.get().format(dto.getCreateTime()))
                .append(" ")
                .append("[")
                .append(dto.getTraceId())
                .append("@")
                .append(dto.getTraceLevel())
                .append("]")
                .append(" ")
                .append("[")
                .append(dto.getSrcSystem())
                .append("/").append(dto.getSrcModule())
                .append("/").append(dto.getSrcLabel())
                .append("]")
                .append(" ")
                .append("[")
                .append(dto.getUserId())
                .append("@")
                .append(dto.getClientIp())
                .append("]")
                .append(" ")
                .append("[")
                .append(dto.getJavaMethod())
                .append("]")
                .append(" ")
                .append("[")
                .append(dto.getRequestType())
                .append(" ")
                .append(dto.getRequestUrl())
                .append("]")
                .append(" ")
                .append("[")
                .append(dto.getLogKey())
                .append(":").append(dto.getLogVal())
                .append("]")
                .append(" ")
                .append(dto.getLogContent())
                .append("\n")
                .append(dto.getExceptClass())
                .append(":")
                .append("\n")
                .append(dto.getExceptStack());

        return builder.toString();
    }

    public static boolean isWindows() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("window");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name", "unknown").toLowerCase().contains("linux");
    }

    public static String getCmdCharset() {
        String charset = System.getProperty("sun.jnu.encoding");
        if (charset == null || "".equals(charset)) {
            charset = System.getProperty("file.encoding");
        }
        if (charset == null || "".equals(charset)) {
            charset = Charset.defaultCharset().name();
        }
        return charset;
    }

    public static String subline(String str, int lineIndex, int lineCount) {
        String[] arr = str.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lineCount; i++) {
            if (i + lineIndex >= arr.length) {
                break;
            }
            builder.append(arr[i + lineIndex]);
            builder.append("\n");
        }
        return builder.toString();
    }


    public static LinuxIostatDto getLinuxIostatXk() {
        /**
         Linux 3.10.0-693.el7.x86_64 (dsj-hadoop-66)     02/13/2023      _x86_64_        (4 CPU)

         avg-cpu:  %user   %nice %system %iowait  %steal   %idle
         7.78    0.00    0.32    0.01    0.00   91.89

         Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
         sda               0.00     0.05    0.01    0.75     0.31     4.88    13.73     0.00    0.54   11.29    0.43   0.15   0.01
         sdb               0.00     0.87    0.04    0.91     0.63    35.04    75.39     0.00    1.41   10.29    1.02   0.33   0.03
         */
        if (!isLinux()) {
            return null;
        }

        LinuxIostatDto retDto = new LinuxIostatDto();
        List<LinuxIostatItemDto> ret = new ArrayList<>();
        retDto.items = ret;

        String str = runCmd("iostat -x -k");
        str = str.trim();
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 3) {
                String[] arr = line.trim().split("\\s+", 6);
                try {
                    retDto.userPer = Double.parseDouble(arr[0]);
                    retDto.nicePer = Double.parseDouble(arr[1]);
                    retDto.systemPer = Double.parseDouble(arr[2]);
                    retDto.iowaitPer = Double.parseDouble(arr[3]);
                    retDto.stealPer = Double.parseDouble(arr[4]);
                    retDto.idlePer = Double.parseDouble(arr[5]);
                } catch (Exception e) {

                }
            }
            if (i < 6) {
                continue;
            }

            String[] arr = line.trim().split("\\s+", 14);

            if (arr.length != 14) {
                continue;
            }

            LinuxIostatItemDto dto = new LinuxIostatItemDto();

            dto.device = arr[0];
            try {
                dto.rrqms = Double.parseDouble(arr[1]);
                dto.wrqms = Double.parseDouble(arr[2]);
                dto.rs = Double.parseDouble(arr[3]);
                dto.ws = Double.parseDouble(arr[4]);
                dto.rKbs = Double.parseDouble(arr[5]);
                dto.wKbs = Double.parseDouble(arr[6]);
                dto.avgRqSz = Double.parseDouble(arr[7]);
                dto.avgQuSz = Double.parseDouble(arr[8]);
                dto.await = Double.parseDouble(arr[9]);
                dto.rAwait = Double.parseDouble(arr[10]);
                dto.wAwait = Double.parseDouble(arr[11]);
                dto.svctm = Double.parseDouble(arr[12]);
                dto.utilPer = Double.parseDouble(arr[13]);
            } catch (Exception e) {

            }

            ret.add(dto);
        }

        return retDto;
    }


    public static List<LinuxDfDto> getLinuxDf() {
        /**
         Filesystem               1K-blocks     Used Available Use% Mounted on
         /dev/mapper/rhel-root    104806400 25646992  79159408  25% /
         devtmpfs                   8118132        0   8118132   0% /dev
         tmpfs                      8134020        0   8134020   0% /dev/shm
         */
        List<LinuxDfDto> ret = new ArrayList<>();
        if (!isLinux()) {
            return ret;
        }

        String str = runCmd("df");
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                continue;
            }
            String line = lines[i];
            String[] arr = line.trim().split("\\s+", 6);

            if (arr.length != 6) {
                continue;
            }
            LinuxDfDto dto = new LinuxDfDto();
            dto.fileSystem = arr[0];
            try {
                dto.blocks1k = Long.parseLong(arr[1]);
                dto.used = Long.parseLong(arr[2]);
                dto.available = Long.parseLong(arr[3]);
            } catch (Exception e) {

            }
            try {
                if (arr[4].endsWith("%")) {
                    String perc = arr[4].substring(0, arr[4].length() - 1);
                    dto.usePercent = Double.parseDouble(perc);
                }
            } catch (Exception e) {

            }
            dto.mountedOn = arr[5];

            ret.add(dto);
        }

        return ret;
    }

    public static String runCmd(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            process.waitFor();

            String str = new String(bos.toByteArray(), getCmdCharset());
            return str;
        } catch (Exception e) {

        }
        return null;
    }


    public static LinuxTop5Dto getLinuxTop5() {
        if (!isLinux()) {
            return null;
        }
        String cmd = runCmd("top -c -b -n 1");
        cmd = subline(cmd, 0, 5);
        return getLinuxTop5(cmd);
    }


    public static LinuxTop5Dto getLinuxTop5(String cmd) {
        /**
         top - 10:28:31 up 228 days, 13:37,  5 users,  load average: 0.92, 0.51, 0.44
         Tasks: 268 total,   1 running, 267 sleeping,   0 stopped,   0 zombie
         %Cpu(s): 27.3 us,  4.5 sy,  0.0 ni, 68.2 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
         KiB Mem : 16268044 total,   169748 free, 13275500 used,  2822796 buff/cache
         KiB Swap:  8380412 total,  5365312 free,  3015100 used.  2360968 avail Mem
         */
        if (cmd == null) {
            return null;
        }
        LinuxTop5Dto ret = new LinuxTop5Dto();
        String cmdRs = cmd.trim();
        String[] lines = cmdRs.split("\n");
        for (String item : lines) {
            String line = item.toLowerCase().trim();
            if (line.startsWith("top")) {
                String[] arr = line.split("-", 2);
                if (arr.length == 2) {
                    String val = arr[1].trim();
                    String[] parts = val.split(",", 4);
                    if (parts.length == 4) {

                        String[] times = parts[0].trim().split("\\s*up\\s*", 2);
                        if (times.length == 2) {
                            ret.topDate = times[0].trim();
                            try {
                                ret.topDays = Integer.parseInt(times[1].trim().split("\\s+")[0]);
                            } catch (Exception e) {
                            }
                        }

                        ret.topTime = parts[1].trim();

                        try {
                            ret.topUsers = Integer.parseInt(parts[2].trim().split("\\s+")[0]);
                        } catch (Exception e) {
                        }

                        try {
                            String average = parts[3].trim().split(":", 2)[1].trim();
                            String[] avgArr = average.split("\\s*\\,\\s*");
                            ret.loadAverage1 = Double.parseDouble(avgArr[0]);
                            ret.loadAverage2 = Double.parseDouble(avgArr[1]);
                            ret.loadAverage3 = Double.parseDouble(avgArr[2]);
                        } catch (Exception e) {
                        }

                    }

                }

            } else if (line.startsWith("tasks:")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String total = null;
                String running = null;
                String sleeping = null;
                String stopped = null;
                String zombie = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("running".equals(kv[1])) {
                        running = kv[0];
                    }
                    if ("sleeping".equals(kv[1])) {
                        sleeping = kv[0];
                    }
                    if ("stopped".equals(kv[1])) {
                        stopped = kv[0];
                    }
                    if ("zombie".equals(kv[1])) {
                        zombie = kv[0];
                    }
                }
                try {
                    ret.tasksTotal = Integer.parseInt(total);
                } catch (Exception e) {
                }
                try {
                    ret.tasksRunning = Integer.parseInt(running);
                } catch (Exception e) {
                }
                try {
                    ret.tasksSleeping = Integer.parseInt(sleeping);
                } catch (Exception e) {
                }
                try {
                    ret.tasksStopped = Integer.parseInt(stopped);
                } catch (Exception e) {
                }
                try {
                    ret.tasksZombie = Integer.parseInt(zombie);
                } catch (Exception e) {
                }
            } else if (line.startsWith("%cpu(s):")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String us = null;
                String sy = null;
                String ni = null;
                String id = null;
                String wa = null;
                String hi = null;
                String si = null;
                String st = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("us".equals(kv[1])) {
                        us = kv[0];
                    }
                    if ("sy".equals(kv[1])) {
                        sy = kv[0];
                    }
                    if ("ni".equals(kv[1])) {
                        ni = kv[0];
                    }
                    if ("id".equals(kv[1])) {
                        id = kv[0];
                    }
                    if ("wa".equals(kv[1])) {
                        wa = kv[0];
                    }
                    if ("hi".equals(kv[1])) {
                        hi = kv[0];
                    }
                    if ("si".equals(kv[1])) {
                        si = kv[0];
                    }
                    if ("st".equals(kv[1])) {
                        st = kv[0];
                    }
                }
                try {
                    ret.cpuUs = Double.parseDouble(us);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSy = Double.parseDouble(sy);
                } catch (Exception e) {
                }
                try {
                    ret.cpuNi = Double.parseDouble(ni);
                } catch (Exception e) {
                }
                try {
                    ret.cpuId = Double.parseDouble(id);
                } catch (Exception e) {
                }
                try {
                    ret.cpuWa = Double.parseDouble(wa);
                } catch (Exception e) {
                }
                try {
                    ret.cpuHi = Double.parseDouble(hi);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSi = Double.parseDouble(si);
                } catch (Exception e) {
                }
                try {
                    ret.cpuSt = Double.parseDouble(st);
                } catch (Exception e) {
                }
            } else if (line.startsWith("kib mem :")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*,\\s*");
                String total = null;
                String free = null;
                String used = null;
                String buffCache = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("free".equals(kv[1])) {
                        free = kv[0];
                    }
                    if ("used".equals(kv[1])) {
                        used = kv[0];
                    }
                    if ("buff/cache".equals(kv[1])) {
                        buffCache = kv[0];
                    }
                }
                try {
                    ret.kibMemTotal = Long.parseLong(total);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemFree = Long.parseLong(free);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemUsed = Long.parseLong(used);
                } catch (Exception e) {
                }
                try {
                    ret.kibMemBuffCache = Long.parseLong(buffCache);
                } catch (Exception e) {
                }
            } else if (line.startsWith("kib swap:")) {
                String[] arr = line.split(":", 2);
                String val = arr[1].trim();
                String[] vals = val.split("\\s*[,|\\.]\\s*");
                String total = null;
                String free = null;
                String used = null;
                String availMem = null;
                for (String task : vals) {
                    String[] kv = task.trim().split("\\s+", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if ("total".equals(kv[1])) {
                        total = kv[0];
                    }
                    if ("free".equals(kv[1])) {
                        free = kv[0];
                    }
                    if ("used".equals(kv[1])) {
                        used = kv[0];
                    }
                    if ("avail mem".equals(kv[1])) {
                        availMem = kv[0];
                    }
                }
                try {
                    ret.kibSwapTotal = Long.parseLong(total);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapFree = Long.parseLong(free);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapUsed = Long.parseLong(used);
                } catch (Exception e) {
                }
                try {
                    ret.kibSwapAvailMem = Long.parseLong(availMem);
                } catch (Exception e) {
                }
            }

        }
        return ret;
    }

    public static LinuxFreeDto getLinuxFree() {
        if (!isLinux()) {
            return null;
        }
        String cmd = runCmd("free");
        return getLinuxFree(cmd);
    }

    public static LinuxFreeDto getLinuxFree(String cmd) {
        /**
         total        used        free      shared  buff/cache   available
         Mem:       16268044    13121288      225708      185272     2921048     2514952
         Swap:       8380412     3012444     5367968
         */
        if (cmd == null) {
            return null;
        }
        LinuxFreeDto ret = new LinuxFreeDto();
        String cmdRs = cmd.trim();
        String[] lines = cmdRs.split("\n");
        for (String item : lines) {
            String line = item.toLowerCase().trim();
            if (line.startsWith("mem:")) {
                String[] arr = line.split("\\s+");
                String total = arr.length >= 2 ? arr[1] : null;
                String used = arr.length >= 3 ? arr[2] : null;
                String free = arr.length >= 4 ? arr[3] : null;
                String shared = arr.length >= 5 ? arr[4] : null;
                String buffCache = arr.length >= 6 ? arr[5] : null;
                String available = arr.length >= 7 ? arr[6] : null;
                try {
                    ret.memTotal = Long.parseLong(total);
                } catch (Exception e) {

                }
                try {
                    ret.memUsed = Long.parseLong(used);
                } catch (Exception e) {

                }
                try {
                    ret.memFree = Long.parseLong(free);
                } catch (Exception e) {

                }
                try {
                    ret.memShared = Long.parseLong(shared);
                } catch (Exception e) {

                }
                try {
                    ret.memBuffCache = Long.parseLong(buffCache);
                } catch (Exception e) {

                }
                try {
                    ret.memAvailable = Long.parseLong(available);
                } catch (Exception e) {

                }

            } else if (line.startsWith("swap:")) {
                String[] arr = line.split("\\s+");
                String total = arr.length >= 2 ? arr[1] : null;
                String used = arr.length >= 3 ? arr[2] : null;
                String free = arr.length >= 4 ? arr[3] : null;

                try {
                    ret.swapTotal = Long.parseLong(total);
                } catch (Exception e) {

                }
                try {
                    ret.swapUsed = Long.parseLong(used);
                } catch (Exception e) {

                }
                try {
                    ret.swapFree = Long.parseLong(free);
                } catch (Exception e) {

                }
            }
        }
        return ret;
    }
}
