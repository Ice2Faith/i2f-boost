package i2f.core.jvm;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.management.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ltb
 * @date 2022/11/7 10:11
 * @desc
 */
public class JvmUtil {

    private static Boolean debugMode;

    public static boolean isDebugFast() {
        if (debugMode == null) {
            synchronized (JvmUtil.class) {
                debugMode = isDebug();
            }
        }
        return debugMode;
    }

    public static boolean isDebug() {
        String[] args = getVmArguments();
        for (String arg : args) {
            if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
                return true;
            }
        }
        return false;
    }


    public static boolean isAgent(){
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String arg : args) {
            if (arg.startsWith("-javaagent:")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoVerify(){
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String arg : args) {
            if ("-noverify".equals(arg) || "-Xverify:none".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    public static RuntimeMXBean getRuntimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    public static String getPid(){
        String name = getRuntimeMXBean().getName();
        String[] arr = name.split("@",2);
        if(arr.length==2){
            return arr[0];
        }
        return "-1";
    }

    public static String getStartUser(){
        String name = getRuntimeMXBean().getName();
        String[] arr = name.split("@",2);
        if(arr.length==2){
            return arr[1];
        }
        return "";
    }

    public static String getVmName() {
        return getRuntimeMXBean().getVmName();
    }

    public static String getVmVendor() {
        return getRuntimeMXBean().getVmVendor();
    }

    public static String getVmVersion() {
        return getRuntimeMXBean().getVmVersion();
    }

    public static String[] getClassPath() {
        return getRuntimeMXBean().getClassPath().split(";");
    }

    public static List<String> getClassPathJars() {
        String javaHome = System.getenv("JAVA_HOME");
        return Arrays.stream(getClassPath())
                .filter(e -> !e.startsWith(javaHome))
                .filter(e -> e.endsWith(".jar"))
                .map(e -> e.substring(e.lastIndexOf(File.separator) + 1))
                .collect(Collectors.toList());
    }

    public static String[] getBootClassPath() {
        return getRuntimeMXBean().getBootClassPath().split(";");
    }

    public static String[] getLibraryPath() {
        return getRuntimeMXBean().getLibraryPath().split(";");
    }

    public static String[] getVmArguments() {
        return getRuntimeMXBean().getInputArguments().toArray(new String[0]);
    }

    public static Date getVmStartDate() {
        return new Date(getRuntimeMXBean().getStartTime() / 1000);
    }

    public static long getVmUpMilliSeconds() {
        return getRuntimeMXBean().getUptime();
    }

    public static ClassLoadingMXBean getClassLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    public static int getLoadedClassCount() {
        return getClassLoadingMXBean().getLoadedClassCount();
    }

    public static long getTotalLoadedClassCount() {
        return getClassLoadingMXBean().getTotalLoadedClassCount();
    }

    public static long getUnloadedClassCount() {
        return getClassLoadingMXBean().getUnloadedClassCount();
    }

    public static CompilationMXBean getCompilationMXBean() {
        return ManagementFactory.getCompilationMXBean();
    }

    public static long getTotalCompilationMilliSeconds() {
        return getCompilationMXBean().getTotalCompilationTime();
    }

    public static MemoryMXBean getMemoryMXBean() {
        return ManagementFactory.getMemoryMXBean();
    }

    public static MemoryUsage getHeapMemoryUsage() {
        return getMemoryMXBean().getHeapMemoryUsage();
    }

    public static MemoryUsage getNonHeapMemoryUsage() {
        return getMemoryMXBean().getNonHeapMemoryUsage();
    }

    public static int getObjectPendingFinalizationCount() {
        return getMemoryMXBean().getObjectPendingFinalizationCount();
    }

    public static OperatingSystemMXBean getOperatingSystemMXBean() {
        return ManagementFactory.getOperatingSystemMXBean();
    }

    public static String getOsName() {
        return getOperatingSystemMXBean().getName();
    }

    public static String getOsArch() {
        return getOperatingSystemMXBean().getArch();
    }

    public static String getOsVersion() {
        return getOperatingSystemMXBean().getVersion();
    }

    public static int getOsProcessorCount() {
        return getOperatingSystemMXBean().getAvailableProcessors();
    }

    public static ThreadMXBean getThreadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    public static int getPeakThreadCount() {
        return getThreadMXBean().getPeakThreadCount();
    }

    public static long[] getAllThreadIds() {
        return getThreadMXBean().getAllThreadIds();
    }

    public static int getThreadCount() {
        return getThreadMXBean().getThreadCount();
    }

    public static long getTotalStartedThreadCount() {
        return getThreadMXBean().getTotalStartedThreadCount();
    }

    public static ThreadInfo getThreadInfo(long threadId) {
        return getThreadMXBean().getThreadInfo(threadId);
    }

    public static long getThreadCpuNanoSeconds(long threadId) {
        return getThreadMXBean().getThreadCpuTime(threadId);
    }

    public static long getThreadUserNanoSeconds(long threadId) {
        return getThreadMXBean().getThreadUserTime(threadId);
    }

    //////////////////////////////////////////////////////////////////////////////

    public static String pkgName(Class clazz) {
        if (clazz == null) {
            return null;
        }
        String name = clazz.getName();
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            return name.substring(0, idx);
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public static <T extends Annotation> T getAssignAnnotation(AccessibleObject elem, Class<T> clazz) {
        T ret = getAnnotation(elem, clazz);
        if (ret == null) {
            if (elem instanceof Member) {
                Member mem = (Member) elem;
                Class<?> declaringClass = mem.getDeclaringClass();
                ret = getAnnotation(declaringClass, clazz);
            }
        }
        return ret;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ret = elem.getDeclaredAnnotation(clazz);
        if (ret == null) {
            ret = elem.getAnnotation(clazz);
        }
        return ret;
    }

    public static void loggingLevel2Debug(Class... classes) {
        loggingLevelWhenDebugMode("debug", classes);
    }

    public static void loggingLevel2Debug(String... pkgs) {
        loggingLevelWhenDebugMode("debug", pkgs);
    }

    public static void loggingLevelWhenDebugMode(String level, Class... classes) {
        List<String> pkgs = new ArrayList<>();
        for (Class item : classes) {
            String pkg = pkgName(item);
            pkgs.add(pkg);
        }
        loggingLevelWhenDebugMode(level, pkgs);
    }

    public static void loggingLevelWhenDebugMode(String level, String... pkgs) {
        loggingLevelWhenDebugMode(level, Arrays.asList(pkgs));
    }

    public static void loggingLevelWhenDebugMode(String level, List<String> pkgs) {
        if (level == null || "".equals(level)) {
            level = "info";
        }
        if (isDebugFast()) {
            for (String item : pkgs) {
                String pkg = item;
                if (pkg == null) {
                    pkg = "root";
                }
                String conf = "logging.level." + pkg;
                System.out.println(conf + "=" + level + ", has assigned");
                System.setProperty(conf, level);
            }
        }
    }

}
