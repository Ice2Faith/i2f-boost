package i2f.core.j2ee.firewall.std.impl.crlf;

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
 * @desc crlf 是在不允许出现换行的地方出现了换行的漏洞
 * 简单说就是，比如在请求头，响应头等本应该只有一行的结构中
 * 通过显式或者隐式的方式添加换行符号
 * 达到篡改头部，或者贯穿到HTML层面，从而添加HTML的方式
 * crlf实际上就是\r\n
 * 是HTTP报文的分割符号
 * 因此，如果一个头部中包含了\r\n
 * 在HTTP看来，那就是两个头部
 * 如果这个头部刚好是最后一行头部
 * 则后面的内容会被视为响应体正文
 * 也就是被当做HTML来解释
 * <p>
 * 解决方式也比较直接
 * 直接判断是否存在\r\n及其变体即可
 * 变体包括但不限于URL编码、HTML编码、字符编码等
 */
public class CrlfFirewallAsserter implements IStringFirewallAsserter {
    public static final char[] BAD_CHARS = {'\r', '\n', (char) 0};
    public static final String[] BAD_STRS = {"\r\n"};

    public static final CrlfFirewallAsserter INSTANCE = new CrlfFirewallAsserter();
    private Context context;
    private boolean useCombine = true;

    public CrlfFirewallAsserter() {
        this.applyRules(null);
    }

    public CrlfFirewallAsserter(Rules rules) {
        this.applyRules(rules);
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

    public static void main(String[] args) {
        List<List<Integer>> groups = FirewallAsserterUtils.getAllCombinations(3);
        System.out.println(groups);

        CrlfFirewallAsserter.INSTANCE.doAssert("clrf", "%25%20\\x25\\x20&#x25;&#32;\\u0025\\u0020&nbsp;&emsp;|%250a;");
    }

    public CrlfFirewallAsserter applyRules(Rules rules) {
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

    public CrlfFirewallAsserter applyCombine(boolean enable) {
        this.useCombine = enable;
        return this;
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        String line = value;

        line = line.toLowerCase();


        char[] badChars = context.badChars;
        if (badChars != null) {
            for (char ch : badChars) {
                String str = ch + "";
                String vstr = containsInjectForm(line, str, useCombine, null);
                if (vstr != null) {
                    throw new CrlfFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
                }
            }
        }

        List<String> badStrs = context.badStrs;
        if (badStrs != null) {
            for (String badStr : badStrs) {
                String str = badStr;
                String vstr = containsInjectForm(line, str, useCombine, null);
                if (vstr != null) {
                    throw new CrlfFirewallException(errorMsg + ", " + " contains illegal str [" + vstr + "]");
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
        public List<String> badSuffixes;
        public List<String> badFilenames;

        public static Context defaultInstance() {
            Context ret = new Context();
            ret.badChars = FirewallAsserterUtils.merge(BAD_CHARS, null, null, null);
            ret.badStrs = FirewallAsserterUtils.merge(Arrays.asList(BAD_STRS), null, null, null);
            return ret;
        }
    }
}
