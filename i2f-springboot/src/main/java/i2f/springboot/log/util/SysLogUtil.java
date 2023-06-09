package i2f.springboot.log.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.consts.LogKeys;
import i2f.springboot.log.consts.LogLevel;
import i2f.springboot.log.consts.LogOperateType;
import i2f.springboot.log.consts.LogType;
import i2f.springboot.log.core.SpringContextProvider;
import i2f.springboot.log.data.SysLogDto;
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
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

}
