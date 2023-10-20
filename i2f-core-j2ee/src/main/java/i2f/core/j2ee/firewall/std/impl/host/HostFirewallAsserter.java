package i2f.core.j2ee.firewall.std.impl.host;

import i2f.core.codec.str.code.UCodeStringCodec;
import i2f.core.codec.str.code.XCodeStringCodec;
import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.codec.str.url.UrlStringStringCodec;
import i2f.core.j2ee.firewall.std.common.FirewallAsserterUtils;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:21
 * @desc host 校验
 * 针对IP地址或者主机名进行的校验
 */
public class HostFirewallAsserter implements IStringFirewallAsserter {


    public static final char[] BAD_CHARS = {'|', '!', '~', '#', '%', '^', '*', '(', ')', '{', '}', '[', ']', ',', '?', '/', '=', '+', '`', '\'', '\"', '\\', '$', '>', '<', ';', (char) 0};
    public static final String[] BAD_STRS = {"//", "\\"};
    public static final HostFirewallAsserter INSTANCE = new HostFirewallAsserter();
    private Context context;
    private boolean useCombine = true;
    private boolean strict = false;

    public HostFirewallAsserter() {
        this.applyRules(null);
    }

    public HostFirewallAsserter(Rules rules) {
        this.applyRules(rules);
    }

    public static void main(String[] args) {
        INSTANCE.doAssert("test", "%2524");
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

    public HostFirewallAsserter applyRules(Rules rules) {
        this.context = Context.defaultInstance();
        if (rules != null) {
            this.context.badChars = FirewallAsserterUtils.merge(
                    BAD_CHARS,
                    rules.includeBadChars,
                    rules.excludeBadChars,
                    rules.replaceBadChars);
            this.context.badStrs = FirewallAsserterUtils.merge(
                    Arrays.asList(BAD_STRS),
                    Arrays.asList(rules.includeBadStrs),
                    Arrays.asList(rules.excludeBadStrs),
                    Arrays.asList(rules.replaceBadStrs));

        }
        return this;
    }

    public HostFirewallAsserter applyCombine(boolean enable) {
        this.useCombine = enable;
        return this;
    }

    public HostFirewallAsserter applyStrict(boolean enable) {
        this.strict = enable;
        return this;
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        String filePath = value;

        filePath = filePath.trim();
        if ("".equals(filePath)) {
            return;
        }

        filePath = filePath.toLowerCase();


        char[] badChars = context.badChars;
        if (badChars != null) {
            for (char ch : badChars) {
                String str = ch + "";
                String vstr = containsInjectForm(filePath, str, useCombine, null);
                if (vstr != null) {
                    throw new HostFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
            }
        }


        for (int i = 0; i < 32; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(filePath, str, useCombine, null);
            if (vstr != null) {
                throw new HostFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }
        for (int i = 127; i < 128; i++) {
            char ch = (char) i;
            String str = ch + "";
            String vstr = containsInjectForm(filePath, str, useCombine, null);
            if (vstr != null) {
                throw new HostFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
            }
        }


        List<String> badStrs = context.badStrs;
        if (badStrs != null) {
            for (String badStr : badStrs) {
                String str = badStr;
                String vstr = containsInjectForm(filePath, str, useCombine, null);
                if (vstr != null) {
                    throw new HostFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
            }
        }

    }

    public static class Rules {
        public char[] includeBadChars;
        public char[] excludeBadChars;
        public char[] replaceBadChars;

        public String[] includeBadStrs;
        public String[] excludeBadStrs;
        public String[] replaceBadStrs;

    }

    public static class Context {
        public char[] badChars;
        public List<String> badStrs;

        public static Context defaultInstance() {
            Context ret = new Context();
            ret.badChars = FirewallAsserterUtils.merge(BAD_CHARS, null, null, null);
            ret.badStrs = FirewallAsserterUtils.merge(Arrays.asList(BAD_STRS), null, null, null);
            return ret;
        }
    }
}
