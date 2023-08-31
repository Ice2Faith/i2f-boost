package i2f.core.codec.str.code;

import i2f.core.codec.str.IStringStringCodec;
import i2f.core.match.regex.RegexUtil;
import i2f.core.match.regex.data.RegexFindPartMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:24
 * @desc utf8编码 % 编码格式
 * % xx
 */
public class UrlCodeStringCodec implements IStringStringCodec {
    public static final String MATCH_REGEX = "%[0-9a-fA-F]{2}";
    public static UrlCodeStringCodec INSTANCE = new UrlCodeStringCodec(true, "UTF-8", new char[]{'/', '-', '/'});
    public static UrlCodeStringCodec URL_COMPONENT_INSTANCE = new UrlCodeStringCodec(true, "UTF-8", new char[0]);
    private boolean jumpAlpha = true;
    private String charset = "UTF-8";
    private char[] excludeChars = {};

    public UrlCodeStringCodec() {

    }

    public UrlCodeStringCodec(boolean jumpAlpha) {
        this.jumpAlpha = jumpAlpha;
    }

    public UrlCodeStringCodec(boolean jumpAlpha, String charset) {
        this.jumpAlpha = jumpAlpha;
        this.charset = charset;
    }

    public UrlCodeStringCodec(boolean jumpAlpha, String charset, char[] excludeChars) {
        this.jumpAlpha = jumpAlpha;
        this.charset = charset;
        this.excludeChars = excludeChars;
    }

    @Override
    public String encode(String data) {
        if (data == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try {
            byte[] bytes = data.getBytes(charset);
            for (byte ch : bytes) {
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
                    builder.append((char) ch);
                    continue;
                }
                builder.append(String.format("%%%02x", (int) (ch & 0x0ff)));
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
        return builder.toString();
    }

    @Override
    public String decode(String enc) {
        if (enc == null) {
            return null;
        }
        List<RegexFindPartMeta> parts = RegexUtil.regexFindParts(enc, MATCH_REGEX);
        StringBuilder builder = new StringBuilder();
        try {
            int len = parts.size();
            int i = 0;
            while (i < len) {
                RegexFindPartMeta item = parts.get(i);
                if (!item.isMatch) {
                    builder.append(item.part);
                    i++;
                } else {
                    List<String> nexts = new LinkedList<>();
                    int j = i;
                    while (j < len) {
                        RegexFindPartMeta next = parts.get(j);
                        if (!next.isMatch) {
                            if (!"".equals(next.part.trim())) {
                                break;
                            }
                        }
                        if (!"".equals(next.part.trim())) {
                            nexts.add(next.part);
                        }
                        j++;
                    }
                    int size = nexts.size();
                    if (size > 0) {
                        byte[] buf = new byte[size];
                        for (int k = 0; k < size; k++) {
                            int val = Integer.parseInt(nexts.get(k).substring("%".length()), 16);
                            buf[k] = (byte) (val & 0x0ff);
                        }
                        builder.append(new String(buf, charset));
                    }
                    i = j;
                }
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }

        return builder.toString();
    }
}
