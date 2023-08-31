package i2f.core.j2ee.firewall.std.impl.serialize;

import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;
import i2f.core.match.regex.RegexUtil;
import i2f.core.match.regex.data.RegexMatchItem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:21
 * @desc 反序列化漏洞
 * 这类漏洞，在java中的表现
 * 最多的一点就是，利用反序列化提供的某些特性
 * 实例化某些特殊的类，达成目的
 * 比如，实例化Process,并指定构造参数
 * 则就有可能在调用这些类的构造，setter等方法时，触发这些类执行一些事情
 * 甚至有一些序列化，还支持调用指定类的指定方法的
 * 更是十分危险
 * 这类漏洞，在java的层面来说
 * 可以在反序列化之前，针对报文正文，检查某些危险的类名
 * 拦截以防止出现危险的调用
 * 说到这里
 */
public class SerializeFirewallAsserter implements IStringFirewallAsserter {
    public static final String CLASS_MATCH_PATTEN = "[a-zA-Z0-9-_$]+(\\.[a-zA-Z0-9-_$]+)+";
    public static final String DEFAULT_JDK_PKGS = "java.,javax.,javafx.,com.sun.,sun.,oracle.,jdk.,com.oracle.,org.ietf,org.jcp,org.omg,org.w3c,org.xml,org.relaxng";

    public static void assertEntry(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        List<Function<String, String>> wrappers = Arrays.asList(
                (str) -> str,
                HtmlStringStringCodec.INSTANCE::decode
        );
        for (Function<String, String> wrapper : wrappers) {
            String text = wrapper.apply(value);
            List<RegexMatchItem> matchItems = RegexUtil.regexFinds(text, CLASS_MATCH_PATTEN);
            for (RegexMatchItem item : matchItems) {
                String className = item.matchStr;
                assertClassname(errorMsg, className);
            }
        }
    }

    public static boolean isHasPkgPrefix(String className, String checkPkgs) {
        if (className == null) {
            return false;
        }
        String[] arr = checkPkgs.split(",");
        for (String item : arr) {
            String str = item.trim();
            if ("".equals(str)) {
                continue;
            }
            if (className.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> findClass(String className) {
        if (className == null || "".equals(className)) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Throwable e) {

        }
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Throwable e) {

        }
        return null;
    }

    public static boolean typeOfAny(Class<?> target, Class<?>... parents) {
        if (target == null) {
            return false;
        }
        for (Class<?> parent : parents) {
            if (parent == null) {
                continue;
            }
            if (parent.equals(target)) {
                return true;
            }
            if (parent.isAssignableFrom(target)) {
                return true;
            }
        }
        return false;
    }

    public static void assertClassname(String errorMsg, String className) {
        if (className == null || "".equals(className)) {
            return;
        }
        if (isHasPkgPrefix(className, DEFAULT_JDK_PKGS)) {
            boolean ok = true;
            if (ok) {
                Class<?> clazz = findClass(className);
                if (clazz != null) {
                    if (!typeOfAny(clazz,
                            String.class, StringBuilder.class, StringBuffer.class, StringJoiner.class,
                            Number.class, Byte.class, Character.class, Boolean.class,
                            int.class, long.class, short.class, byte.class, char.class, boolean.class,
                            float.class, double.class,
                            java.util.Date.class, Calendar.class,
                            Temporal.class,
                            InputStream.class, OutputStream.class,
                            Reader.class, Writer.class,
                            Annotation.class)) {
                        ok = false;
                    }
                }
            }
            if (!ok) {
                throw new SerializeFirewallException(errorMsg + ", " + " contains illegal class [" + className + "]");
            }
        }
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        assertEntry(errorMsg, value);
    }

}
