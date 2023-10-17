package i2f.core.j2ee.firewall.std.impl.serialize;

import i2f.core.codec.str.code.UCodeStringCodec;
import i2f.core.codec.str.code.XCodeStringCodec;
import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.codec.str.url.UrlStringStringCodec;
import i2f.core.j2ee.firewall.std.common.FirewallAsserterUtils;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;

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
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final String JAVA_NAME_PATTEN = "[a-zA-Z0-9-_\\$\\.]+";
    public static final String CLASS_MATCH_PATTEN = "[a-zA-Z0-9-_\\$]+(\\.[a-zA-Z0-9-_\\$]+)+";
    public static final String DEFAULT_JDK_PKGS = "java.,javax.,javafx.,com.sun.,sun.,oracle.,jdk.,com.oracle.,org.ietf,org.jcp,org.omg,org.w3c,org.xml,org.relaxng";
    public static final String[] BAD_MATCHES = {
            CLASS_MATCH_PATTEN,
            JAVA_NAME_PATTEN + "executor",
            JAVA_NAME_PATTEN + "runner",
            JAVA_NAME_PATTEN + "processor",
            JAVA_NAME_PATTEN + "runtime",
            JAVA_NAME_PATTEN + "connector",
            JAVA_NAME_PATTEN + "connection",
            JAVA_NAME_PATTEN + "listener",
            JAVA_NAME_PATTEN + "parser",
    };
    public static final SerializeFirewallAsserter INSTANCE = new SerializeFirewallAsserter();

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

    private Context context;
    private boolean useCombine = true;

    public SerializeFirewallAsserter() {
        this.applyRules(null);
    }

    public SerializeFirewallAsserter(Rules rules) {
        this.applyRules(rules);
    }

    public static void main(String[] args) {
        String className = "org.springframework.ApplicationRunner";
        INSTANCE.doAssert("serialize", className);
    }

    public static String containsInjectFormByEncode(String targetStr, String testStr, boolean useCombine) {
        if (testStr == null || "".equals(testStr)) {
            return null;
        }
        List<Function<String, String>> singleWrappers = Arrays.asList(
                (str) -> str,
                UrlStringStringCodec.INSTANCE::encode,
                UrlStringStringCodec.INSTANCE::encode,
                FirewallAsserterUtils.ENCODE_0X_02X,
                FirewallAsserterUtils.ENCODE_PER_02X,
                FirewallAsserterUtils.ENCODE_XCODE_02X,
                FirewallAsserterUtils.ENCODE_UCODE_04X,
                HtmlStringStringCodec.INSTANCE::encode,
                UCodeStringCodec.INSTANCE::encode,
                XCodeStringCodec.INSTANCE::encode
        );

        List<Function<String, String>> wrappers = FirewallAsserterUtils.combinationsWrappers(singleWrappers, useCombine);

        for (Function<String, String> wrapper : wrappers) {
            try {
                String text = wrapper.apply(testStr);
                text = text.toLowerCase();
                if (targetStr.contains(text)) {
                    return text;
                }
            } catch (Exception e) {

            }
        }

        return null;
    }

    public static String containsInjectFormByDecode(String targetStr, String testStr, boolean useCombine, String testMatchStr) {
        boolean normalEq = false;
        if (testStr != null && !"".equals(testStr)) {
            normalEq = true;
        }

        Pattern pattern = null;
        if (testMatchStr != null && !"".equalsIgnoreCase(testMatchStr)) {
            pattern = Pattern.compile(testMatchStr);
        }

        if (!normalEq && pattern == null) {
            return null;
        }

        List<Function<String, String>> singleWrappers = Arrays.asList(
                (str) -> str,
                UrlStringStringCodec.INSTANCE::decode,
                UrlStringStringCodec.INSTANCE::decode,
                HtmlStringStringCodec.INSTANCE::decode,
                UCodeStringCodec.INSTANCE::decode,
                XCodeStringCodec.INSTANCE::decode
        );

        List<Function<String, String>> wrappers = FirewallAsserterUtils.combinationsWrappers(singleWrappers, useCombine);

        for (Function<String, String> wrapper : wrappers) {
            try {
                String text = wrapper.apply(targetStr);
                text = text.toLowerCase();
                if (normalEq) {
                    if (text.contains(testStr)) {
                        return testStr;
                    }
                }
                if (pattern != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        MatchResult rs = matcher.toMatchResult();
                        String vstr = text.substring(rs.start(), rs.end());
                        return vstr;
                    }
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static String containsInjectForm(String targetStr, String testStr, boolean useCombine, String testMatchStr) {
        String vstr = containsInjectFormByEncode(targetStr, testStr, useCombine);
        if (vstr != null) {
            return vstr;
        }
        return containsInjectFormByDecode(targetStr, testStr, useCombine, testMatchStr);
    }

    public SerializeFirewallAsserter applyRules(Rules rules) {
        this.context = Context.defaultInstance();
        if (rules != null) {
            this.context.badMatches = FirewallAsserterUtils.merge(
                    Arrays.asList(BAD_MATCHES),
                    Arrays.asList(rules.includeBadMatches),
                    Arrays.asList(rules.excludeBadMatches),
                    Arrays.asList(rules.replaceBadMatches));

        }
        return this;
    }

    public SerializeFirewallAsserter applyCombine(boolean enable) {
        this.useCombine = enable;
        return this;
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        String content = value;

        content = content.trim();
        if ("".equals(content)) {
            return;
        }


        List<String> badMatches = context.badMatches;
        if (badMatches != null) {
            for (String badMatch : badMatches) {
                String str = badMatch;
                String vstr = containsInjectForm(content, null, useCombine, str);
                if (vstr != null) {
                    if (CLASS_MATCH_PATTEN.equals(badMatch)) {
                        String className = vstr;
                        assertClassname(errorMsg, className);
                    } else {
                        throw new SerializeFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                    }
                }
            }
        }

    }

    public static class Rules {

        public String[] includeBadMatches;
        public String[] excludeBadMatches;
        public String[] replaceBadMatches;

    }

    public static class Context {
        public List<String> badMatches;

        public static Context defaultInstance() {
            Context ret = new Context();
            ret.badMatches = FirewallAsserterUtils.merge(Arrays.asList(BAD_MATCHES), null, null, null);
            return ret;
        }
    }

}
