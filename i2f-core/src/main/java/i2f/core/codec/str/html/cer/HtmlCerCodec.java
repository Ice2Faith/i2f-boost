package i2f.core.codec.str.html.cer;

import i2f.core.codec.str.IStringStringCodec;
import i2f.core.match.regex.RegexUtil;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:24
 * @desc html Character Entities References
 * html 字符实体引用编码
 * '&' + 实体名称 +';' 例如： &nbsp; 就是空格
 */
public class HtmlCerCodec implements IStringStringCodec {
    public static final String MATCH_REGEX = "&[a-zA-Z]([0-9a-zA-Z]+);";
    public static HtmlCerCodec INSTANCE = new HtmlCerCodec();
    private boolean jumpAlpha = true;
    private char[] excludeChars = {};

    public HtmlCerCodec() {

    }

    @Override
    public String encode(String data) {
        if (data == null) {
            return data;
        }
        StringBuilder builder = new StringBuilder();
        char[] chars = data.toCharArray();
        for (char ch : chars) {
            boolean isTarget = true;
            if (jumpAlpha) {
                if ((ch >= 'a' && ch <= 'z')
                        || (ch >= 'A' && ch <= 'Z')
                        || (ch >= '0' && ch <= '9')) {
                    isTarget = false;
                }
            }
            if (isTarget) {
                if (excludeChars != null && excludeChars.length > 0) {
                    for (char ech : excludeChars) {
                        if (ech == ch) {
                            isTarget = false;
                            break;
                        }
                    }
                }
            }
            if (!isTarget) {
                builder.append(ch);
                continue;
            }
            String enc = HtmlCerTableHolder.encode((int) ch);
            builder.append(enc);
        }
        return builder.toString();
    }

    @Override
    public String decode(String enc) {
        if (enc == null) {
            return null;
        }
        String ret = RegexUtil.regexFindAndReplace(enc, MATCH_REGEX, (str) -> {
            str = str.toLowerCase();
            int uni = HtmlCerTableHolder.decode(str);
            if (uni >= 0) {
                return String.valueOf((char) uni);
            } else {
                return str;
            }
        });
        return ret;
    }
}
