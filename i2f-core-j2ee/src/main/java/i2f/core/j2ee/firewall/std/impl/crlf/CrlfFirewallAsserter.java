package i2f.core.j2ee.firewall.std.impl.crlf;

import i2f.core.codec.str.code.UCodeStringCodec;
import i2f.core.codec.str.code.XCodeStringCodec;
import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.codec.str.url.UrlStringStringCodec;
import i2f.core.j2ee.firewall.std.impl.xxe.XxeFirewallException;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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

    public static void assertEntry(String errorMsg, String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        List<Function<String, String>> singleWrappers = Arrays.asList(
                (str) -> str,
                UrlStringStringCodec.INSTANCE::decode,
                HtmlStringStringCodec.INSTANCE::decode,
                UCodeStringCodec.INSTANCE::decode,
                XCodeStringCodec.INSTANCE::decode
        );
        int size = singleWrappers.size();

        List<Function<String, String>> wrappers = new LinkedList<>(singleWrappers);
        boolean useCombine = true;
        if (useCombine) {
            List<Function<String, String>> groupWrappers = new LinkedList<>();
            List<List<Integer>> groups = getGroups(size);
            for (List<Integer> group : groups) {
                Function<String, String> groupWrapper = (str) -> {
                    String ret = str;
                    for (Integer idx : group) {
                        Function<String, String> func = singleWrappers.get(idx);
                        ret = func.apply(ret);
                    }
                    return ret;
                };
                groupWrappers.add(groupWrapper);
            }
            wrappers = groupWrappers;
        }
        for (Function<String, String> wrapper : wrappers) {
            String text = wrapper.apply(value);
            text = text.toLowerCase();
            boolean trimSpace = true;
            if (trimSpace) {
                text = text.replaceAll("\\s+", "");
            }
            System.out.println(text);
            if (text.contains("\n")) {
                throw new XxeFirewallException(errorMsg + ", " + " contains illegal crlf express [" + text + "]");
            }

        }
    }

    public static void main(String[] args) {
        List<List<Integer>> groups = getGroups(4);
        System.out.println(groups);

        assertEntry("clrf", "%25%20\\x25\\x20&#x25;&#32;\\u0025\\u0020&nbsp;&emsp;|");
    }

    private static List<List<Integer>> getGroups(int size) {
        List<List<Integer>> ret = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            // 从size中选区i个元素的组合
            LinkedList<Integer> current = new LinkedList<>();
            getNextGroups(size, i, current, ret);
        }
        return ret;
    }

    private static void getNextGroups(int size, int cnt, LinkedList<Integer> current, List<List<Integer>> ret) {
        int curSize = current.size();
        if (curSize == cnt) {
            ret.add(new LinkedList<>(current));
            return;
        }
        int start = 0;
        if (curSize > 0) {
            start = current.getLast() + 1;
        }
        for (int i = start; i < size; i++) {
            current.add(i);
            getNextGroups(size, cnt, current, ret);
            current.removeLast();
        }
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        assertEntry(errorMsg, value);
    }
}
