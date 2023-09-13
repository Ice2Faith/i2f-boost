package i2f.core.j2ee.firewall.std.impl.xxe;

import i2f.core.codec.str.html.HtmlStringStringCodec;
import i2f.core.j2ee.firewall.std.str.IStringFirewallAsserter;
import i2f.core.match.regex.RegexUtil;
import i2f.core.match.regex.data.RegexMatchItem;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:21
 * @desc xxe 是反序列化xml中可能会遇到的漏洞
 * 利用的就是xml规范的外部实体定义功能
 * 这样，就可以将命令等绑定为一个特殊的xml标记
 * 从而实现命令执行等问题
 * 举例：
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE foo [ <!ENTITY xxe SYSTEM "file:///etc/passwd"> ]>
 * <root>&xxe;</root>
 * 这个例子中，首先利用外部实体<!ENTITY>定义了一个标记，名为xxe,这个标记的作用是，使用系统函数，打开这个URL
 * 然后，在报文正文中，使用了&xxe;这个标记
 * 这最终就会导致，在反序列化这个XML的时候，系统会打开目标URL
 * 从而造成危害
 * 在当下的数据交互过程中，XML仅仅是用来传输数据的
 * 一般不会使用到这种特性
 * 因此，只需要针对报文文本进行判断包含
 * <!DOCTYPE
 * <!ENTITY
 * 即可
 */
public class XxeFirewallAsserter implements IStringFirewallAsserter {
    public static final String XXE_MATCH_PATTEN = "<!(doctype|entity)\\s+";

    public static void disableDomXxeConfig() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);
    }

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
            text = text.toLowerCase();
            List<RegexMatchItem> matchItems = RegexUtil.regexFinds(text, XXE_MATCH_PATTEN);
            for (RegexMatchItem item : matchItems) {
                String express = item.matchStr;
                throw new XxeFirewallException(errorMsg + ", " + " contains illegal xxe express [" + express + "]");
            }
        }
    }

    @Override
    public void doAssert(String errorMsg, String value) {
        assertEntry(errorMsg, value);
    }
}
