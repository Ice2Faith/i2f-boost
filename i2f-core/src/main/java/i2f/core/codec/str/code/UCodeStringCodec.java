package i2f.core.codec.str.code;

import i2f.core.codec.str.IStringStringCodec;
import i2f.core.match.regex.RegexUtil;
import i2f.core.match.regex.data.RegexFindPartMeta;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:24
 * @desc unicode编码 \ u 编码格式
 * \ u xxxx
 * 例如： \u0020 就是空格
 * 因为要求是4位16进制
 * 最大表示到0xffff
 * 那么大于这个值的怎么办呢？
 * 一般的时候，是拆分为两个编码进行表示
 * 如果 unicode编码 <= 0xFFFF , 直接用两个字节存unicode编码
 * 如果 unicode编码 > 0xFFFF ,
 * 先计算 U = unicode编码 - 0x10000，
 * 然后将 U 写成二进制形式：yyyy yyyy yyxx xxxx xxxx ，
 * 接着用4个字节这样存：110110yyyyyyyyyy 110111xxxxxxxxxx ，
 * 那么，举个例子：
 * // 字符，大于0xffff==65535
 * int ch=119997;
 * // 减去0x10000
 * int dch=ch-0x10000;
 * // 第一个字符，需要移除x的10位之后取10位
 * // 也就是右移10位，之后取出低10位，也就是&0x03ff
 * int d1=(dch>>>10)&0x03ff;
 * // 然后，第一个字符的高6位为110110==0x36
 * // 再或上0x36<<10
 * d1|=0x36<<10;
 * // 第二个字符，直接取低10位，也就是直接&0x03ff
 * int d2=dch&0x03ff;
 * // 高6位需要110111==0x37
 * // 再或上0x37<<10
 * d2|=0x37<<10;
 * System.out.println(String.format("\\u%04x\\u%04x",d1,d2));
 * <p>
 * 解码
 * 也就是上述过程的逆过程
 * 判断高6位是否是110110==0x36
 * 如果不是0x36,则表示是完整的单个u码
 * 如果是0x36,则表示是需要下一个做联合，是需要两个u码
 * 为了严谨，下一个u码应该是0x37开头
 * 但是，在JS标准ES6中，提出了另一种不拆分的方式
 * u{12a45}
 * 就是采用大括号包含的方式，将完整的unicode编码放入其中
 */
public class UCodeStringCodec implements IStringStringCodec {
    public static final String MATCH_REGEX = "\\\\[u|U][{]?[0-9a-fA-F]{4,8}[}]?";
    public static UCodeStringCodec INSTANCE = new UCodeStringCodec();
    private boolean jumpAlpha = true;
    private char[] excludeChars = {};

    public UCodeStringCodec() {

    }

    public UCodeStringCodec(boolean jumpAlpha) {
        this.jumpAlpha = jumpAlpha;
    }

    public UCodeStringCodec(boolean jumpAlpha, char[] excludeChars) {
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
            int cv = (int) ch;
            if (cv > 0xffff) {
                int dch = cv - 0x10000;
                int d1 = ((dch >>> 10) & 0x03ff) | (0x36 << 10);
                int d2 = (dch & 0x03ff) | (0x37 << 10);
                builder.append(String.format("\\u%04x\\u%04x", d1, d2));
            } else {
                builder.append(String.format("\\u%04x", cv));
            }
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
        int len = parts.size();
        int i = 0;
        while (i < len) {
            RegexFindPartMeta item = parts.get(i);
            if (!item.isMatch) {
                builder.append(item.part);
                i++;
            } else {
                String str = item.part.substring("\\u".length());
                boolean isEs6 = false;
                if (str.startsWith("{")) {
                    str = str.substring(1);
                    isEs6 = true;
                }
                if (str.endsWith("}")) {
                    str = str.substring(0, str.length() - 1);
                    isEs6 = true;
                }
                int val = Integer.parseInt(str, 16);
                if (isEs6) {
                    char ch = (char) val;
                    builder.append(ch);
                    i++;
                } else {
                    int flag = (val >> 10) & 0x03f;
                    if (flag == 0x36) {
                        // 是需要两位才能组成完成char
                        int j = i + 1;
                        boolean findNext = true;
                        while (j < len) {
                            RegexFindPartMeta next = parts.get(j);
                            if (!next.isMatch) {
                                if (!"".equals(next.part.trim())) {
                                    findNext = false;
                                    break;
                                }
                            }
                            j++;
                        }
                        if (findNext) {
                            RegexFindPartMeta next = parts.get(j);
                            int nextVal = Integer.parseInt(next.part.substring("\\u".length()), 16);
                            int nextFlag = (val >> 10) & 0x03f;
                            if (nextFlag == 0x37) {
                                // 是合法的连接
                                int chv = (val & 0x03ff << 10) | (nextVal & 0x03ff);
                                char ch = (char) chv;
                                builder.append(ch);
                                i = j + 1;
                            } else {
                                // 不是合法的连接
                                char ch = (char) val;
                                builder.append(ch);
                                i++;
                            }
                        } else {
                            // bad char
                            char ch = (char) val;
                            builder.append(ch);
                            i++;
                        }
                    } else {
                        char ch = (char) val;
                        builder.append(ch);
                        i++;
                    }
                }
            }
        }

        return builder.toString();
    }
}
