package i2f.springboot.trace.spy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.trace.spy.data.InvokeTraceMeta;
import i2f.springboot.trace.spy.data.InvokeType;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/5/22 15:23
 * @desc
 */
public class InvokeTrace {
    public static LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static final InheritableThreadLocal<Integer> levelHolder = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<String> traceHolder = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<Integer> stepHolder = new InheritableThreadLocal<>();

    public static AtomicBoolean enable = new AtomicBoolean(true);
    public static AtomicBoolean stackTraceEnable = new AtomicBoolean(false);

    public static Set<Method> excludeMethods = new HashSet<>();

    static {
        Class<Object> clazz = Object.class;
        for (Method method : clazz.getDeclaredMethods()) {
            excludeMethods.add(method);
        }
        for (Method method : clazz.getMethods()) {
            excludeMethods.add(method);
        }
    }


    public static synchronized String getBatchId() {
        String traceId = traceHolder.get();
        if (traceId == null) {
            traceId = genTraceId();
        }
        traceHolder.set(traceId);
        return traceId;
    }

    public static String genTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }


    public static synchronized int getTraceLevel() {
        Integer level = levelHolder.get();
        if (level == null) {
            level = 0;
        }
        levelHolder.set(level);
        return level;
    }

    public static synchronized int nextTraceStep() {
        Integer step = stepHolder.get();
        if (step == null) {
            step = 0;
        }
        step++;
        stepHolder.set(step);
        return step;
    }

    public static synchronized int enterTraceLevel() {
        int lvl = getTraceLevel();
        lvl += 1;
        levelHolder.set(lvl);
        return lvl;
    }

    public static synchronized int leaveTraceLevel() {
        int lvl = getTraceLevel();
        lvl -= 1;
        levelHolder.set(lvl);
        return lvl;
    }


    public interface Executor<T> {
        T exec() throws Throwable;
    }

    public static String getRealClassName(String name) {
        int idx = name.indexOf("$$Enhancer");
        if (idx >= 0) {
            name = name.substring(0, idx);
        }
        return name;
    }

    public static Object trace(Executor<Object> executor, Consumer<InvokeTraceMeta> consumer, Class<?> clazz, Method method, Object... args) throws Throwable {
        if (!enable.get()) {
            return executor.exec();
        }

        if (method != null) {
            if (clazz == null) {
                clazz = method.getDeclaringClass();
            }
        }

        if (method != null) {
            if (excludeMethods.contains(method)) {
                return executor.exec();
            }

        }
        if (method != null) {
            boolean isOverride = false;
            for (Method exm : excludeMethods) {
                if (!exm.getName().equals(method.getName())) {
                    continue;
                }
                if (exm.getParameterCount() != method.getParameterCount()) {
                    continue;
                }
                boolean paramMatched = true;
                int pcnt = exm.getParameterCount();
                for (int i = 0; i < pcnt; i++) {
                    if (!exm.getParameterTypes()[i].isAssignableFrom(method.getParameterTypes()[i])) {
                        paramMatched = false;
                        break;
                    }
                }
                if (paramMatched) {
                    isOverride = true;
                    break;
                }
            }
            if (isOverride) {
                return executor.exec();
            }
        }

        if (clazz != null) {
            if (clazz.getName().startsWith("java.")) {
                return executor.exec();
            }
        }

        String[] names = new String[args.length];

        if (method != null) {
            names = nameDiscoverer.getParameterNames(method);
            if (names == null) {
                Parameter[] params = method.getParameters();
                names = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    names[i] = params[i].getName();
                }
            }
        }

        Thread thread = Thread.currentThread();
        Runtime runtime = Runtime.getRuntime();
        String batchId = getBatchId();

        long beginTs = System.currentTimeMillis();
        int lvl = enterTraceLevel();

        Object retVal = null;
        Throwable thr = null;
        StackTraceElement stack = null;

        if (method != null && stackTraceEnable.get()) {
            StackTraceElement[] stacks = thread.getStackTrace();
            int fidx = -1;
            for (int i = 0; i < stacks.length; i++) {
                String methodName = stacks[i].getMethodName();
                if (method.getName().equals(methodName)) {
                    String clazzName = getRealClassName(clazz.getName());
                    String stackClazz = getRealClassName(stacks[i].getClassName());
                    if (clazzName.equals(stackClazz)) {
                        fidx = i;
                        break;
                    }
                }
            }
            if (fidx >= 0) {
                for (int i = fidx + 1; i < stacks.length; i++) {
                    String className = stacks[i].getClassName();
                    if (className.startsWith("org.springframework.")) {
                        continue;
                    }
                    if (className.startsWith("sun.reflect.")) {
                        continue;
                    }
                    if (className.startsWith("org.apache.catalina.")) {
                        continue;
                    }
                    int lineNumber = stacks[i].getLineNumber();
                    if (lineNumber >= 0) {
                        stack = stacks[i];
                        continue;
                    }
                }
            }
        }
        try {
            InvokeTraceMeta beforeMeta = new InvokeTraceMeta();
            beforeMeta.type = InvokeType.BEFORE;
            beforeMeta.time = LocalDateTime.now();
            beforeMeta.clazz = clazz;
            beforeMeta.method = method;
            beforeMeta.paramNames = names;
            beforeMeta.args = args;
            beforeMeta.beginTs = beginTs;
            beforeMeta.batchId = batchId;
            beforeMeta.level = lvl;
            beforeMeta.step = nextTraceStep();
            beforeMeta.stack = stack;
            beforeMeta.assignThreadInfo(thread);
            beforeMeta.assignRuntimeInfo(runtime);
            consumer.accept(beforeMeta);

            retVal = executor.exec();
            long endTs = System.currentTimeMillis();

            InvokeTraceMeta afterMeta = new InvokeTraceMeta();
            afterMeta.type = InvokeType.AFTER;
            afterMeta.time = LocalDateTime.now();
            afterMeta.clazz = clazz;
            afterMeta.method = method;
            afterMeta.paramNames = names;
            afterMeta.args = args;
            afterMeta.ret = retVal;
            afterMeta.beginTs = beginTs;
            afterMeta.endTs = endTs;
            afterMeta.useTs = endTs - beginTs;
            afterMeta.batchId = batchId;
            afterMeta.level = lvl;
            afterMeta.step = nextTraceStep();
            afterMeta.stack = stack;
            afterMeta.assignThreadInfo(thread);
            afterMeta.assignRuntimeInfo(runtime);
            consumer.accept(afterMeta);

            return retVal;
        } catch (Throwable e) {
            thr = e;
            long endTs = System.currentTimeMillis();

            InvokeTraceMeta thrMeta = new InvokeTraceMeta();
            thrMeta.type = InvokeType.THROW;
            thrMeta.time = LocalDateTime.now();
            thrMeta.clazz = clazz;
            thrMeta.method = method;
            thrMeta.paramNames = names;
            thrMeta.args = args;
            thrMeta.ex = thr;
            thrMeta.beginTs = beginTs;
            thrMeta.endTs = endTs;
            thrMeta.useTs = endTs - beginTs;
            thrMeta.batchId = batchId;
            thrMeta.level = lvl;
            thrMeta.step = nextTraceStep();
            thrMeta.stack = stack;
            thrMeta.assignThreadInfo(thread);
            thrMeta.assignRuntimeInfo(runtime);
            consumer.accept(thrMeta);

            throw e;
        } finally {
            lvl = leaveTraceLevel();
            long endTs = System.currentTimeMillis();

            InvokeTraceMeta finalMeta = new InvokeTraceMeta();
            finalMeta.type = InvokeType.FINALLY;
            finalMeta.time = LocalDateTime.now();
            finalMeta.clazz = clazz;
            finalMeta.method = method;
            finalMeta.paramNames = names;
            finalMeta.args = args;
            finalMeta.ret = retVal;
            finalMeta.ex = thr;
            finalMeta.beginTs = beginTs;
            finalMeta.endTs = endTs;
            finalMeta.useTs = endTs - beginTs;
            finalMeta.batchId = batchId;
            finalMeta.level = lvl;
            finalMeta.step = nextTraceStep();
            finalMeta.stack = stack;
            finalMeta.assignThreadInfo(thread);
            finalMeta.assignRuntimeInfo(runtime);
            consumer.accept(finalMeta);
        }
    }
}
