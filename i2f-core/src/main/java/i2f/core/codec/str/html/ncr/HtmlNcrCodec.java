package i2f.core.codec.str.html.ncr;

import i2f.core.codec.str.IStringStringCodec;
import i2f.core.match.regex.RegexUtil;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:24
 * @desc html Numeric Character Reference
 * html 数值字符引用编码
 * '&#'+十进制+';'  例如： &#32; 就是空格
 * '&#x'+十六进制+';' 例如： &#x20; 就是空格
 */
public class HtmlNcrCodec implements IStringStringCodec {
    public static final HtmlNcrCodec INSTANCE = new HtmlNcrCodec();
    public static final HtmlNcrCodec CER_FOR_INSTANCE = new HtmlNcrCodec(false, true, new char[]{'&', ';'});

    public static final String MATCH_REGEX = "&#((\\d+)|([x|X][0-9a-fA-F]+));";

    private boolean hexMode = false;
    private boolean jumpAlpha = true;
    private char[] excludeChars = {};

    public HtmlNcrCodec() {

    }

    public HtmlNcrCodec(boolean hexMode) {
        this.hexMode = hexMode;
    }

    public HtmlNcrCodec(boolean hexMode, boolean jumpAlpha) {
        this.hexMode = hexMode;
        this.jumpAlpha = jumpAlpha;
    }

    public HtmlNcrCodec(boolean hexMode, boolean jumpAlpha, char[] excludeChars) {
        this.hexMode = hexMode;
        this.jumpAlpha = jumpAlpha;
        this.excludeChars = excludeChars;
    }

    @Override
    public String encode(String data) {
        if (data == null) {
            return null;
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
            if (hexMode) {
                builder.append(String.format("&#x%x;", (int) ch));
            } else {
                builder.append(String.format("&#%d;", (int) ch));
            }
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
            if (str.startsWith("&#x")) {
                str = str.substring("&#x".length(), str.length() - 1);
                return String.valueOf((char) Integer.parseInt(str, 16));
            } else {
                str = str.substring("&#".length(), str.length() - 1);
                return String.valueOf((char) Integer.parseInt(str, 10));
            }
        });
        return ret;
    }
}
