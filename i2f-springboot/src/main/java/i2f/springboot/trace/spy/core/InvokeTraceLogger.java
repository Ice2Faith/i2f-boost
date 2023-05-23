package i2f.springboot.trace.spy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.trace.spy.data.InvokeTraceMeta;
import i2f.springboot.trace.spy.data.InvokeType;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/5/22 16:08
 * @desc
 */
@Slf4j
public class InvokeTraceLogger implements Consumer<InvokeTraceMeta> {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
    public static ObjectMapper objectMapper = new ObjectMapper();

    public long slowMs = 300;
    public boolean showStackTrace = true;

    public boolean showArgs = false;
    public boolean showReturn = false;

    public boolean typeOnly = true;
    public boolean jsonPrint = true;
    public boolean jsonFormat = true;


    public String getPrefix(int level) {
        StringBuilder prefixBuilder = new StringBuilder();
        for (int i = -1; i < level; i++) {
            prefixBuilder.append(" | ");
        }
        return prefixBuilder.toString();
    }

    @Override
    public void accept(InvokeTraceMeta meta) {
        StringBuilder builder = new StringBuilder();
        String prefix = getPrefix(meta.level);
        prefix = prefix + " [" + meta.batchId + "@" + meta.level + "@" + meta.step + "] ";
        if (meta.type == InvokeType.BEFORE) {
            builder.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>").append("\n");
        } else {
            builder.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<").append("\n");
        }
        builder.append(prefix).append("method:").append(meta.method).append("\n");
        if (meta.clazz != null) {
            builder.append(prefix).append("class:").append(meta.clazz).append("\n");
        }
        builder.append(prefix).append("time:").append(dateTimeFormatter.format(meta.time)).append("\n");
        builder.append(prefix).append("thread:").append(meta.threadGroupName).append("/").append(meta.threadName).append("@").append(meta.threadId).append("\n");
        builder.append(prefix).append("memory:").append("useRate:" + meta.memUseRate + "% free=" + (meta.memFree / 1024 / 1024) + "MB total=" + (meta.memTotal / 1024 / 1024) + "MB max=" + (meta.memMax / 1024 / 1024) + "MB").append("\n");
        if (meta.type != InvokeType.BEFORE) {
            builder.append(prefix).append("useTime:").append(meta.useTs).append("ms").append("\n");
        }
        if (meta.remark != null && !"".equals(meta.remark)) {
            builder.append(prefix).append("remark:").append(meta.remark).append("\n");
        }
        if (meta.stack != null) {
            builder.append(prefix).append("stack:").append("at ")
                    .append(meta.stack.getClassName())
                    .append(".")
                    .append(meta.stack.getMethodName())
                    .append("(")
                    .append(meta.stack.getFileName())
                    .append(":")
                    .append(meta.stack.getLineNumber())
                    .append(")")
                    .append("\n");
        }
        if (meta.type == InvokeType.THROW) {
            if (showStackTrace) {
                builder.append(prefix).append("exception:").append(meta.ex.getMessage()).append("\n");
                builder.append(prefix).append("stackTrace:").append(getExceptionStackTraceAsString(meta.ex)).append("\n");
            }
        }
        if (meta.args != null) {
            if (showArgs) {
                for (int i = 0; i < meta.args.length; i++) {
                    builder.append(prefix).append("\targ[" + i + "]/" + meta.paramNames[i] + ":").append(visualAsString(meta.args[i])).append("\n");
                }
            }
        }
        if (showReturn) {
            builder.append(prefix).append("return:").append(visualAsString(meta.ret)).append("\n");
        }

        String str = builder.toString();
        if (meta.type == InvokeType.THROW) {
            log.error("trace exception : " + str);
            return;
        }
        if (meta.type == InvokeType.FINALLY) {
            if (meta.useTs > slowMs) {
                log.warn("trace slow : " + str);
                return;
            }
        }
        if (meta.type == InvokeType.BEFORE) {
            log.info("trace before : " + str);
            return;
        }

        log.info("trace invoke : " + str);

    }

    public static String getExceptionStackTraceAsString(Throwable ex) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        ex.printStackTrace(ps);
        ps.close();
        return new String(bos.toByteArray());
    }


    public String visualAsString(Object obj) {
        if (obj == null) {
            return String.valueOf(obj);
        }
        Class clazz = obj.getClass();
        String fullName = clazz.getName();
        if (typeOnly) {
            return fullName;
        }
        String str = String.valueOf(obj);
        if (fullName.startsWith("org.springframework.")) {
            return str;
        }
        if (fullName.startsWith("java.io.")) {
            return str;
        }
        if (obj instanceof InputStream) {
            return str;
        }
        if (obj instanceof OutputStream) {
            return str;
        }
        if (obj instanceof Reader) {
            return str;
        }
        if (obj instanceof Writer) {
            return str;
        }
        if (!jsonPrint) {
            return str;
        }
        try {
            if (jsonFormat) {
                str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } else {
                str = objectMapper.writeValueAsString(obj);
            }
        } catch (Exception e) {

        }
        return str;
    }
}
