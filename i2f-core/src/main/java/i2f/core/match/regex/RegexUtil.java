package i2f.core.match.regex;


import i2f.core.match.regex.data.RegexFindPartMeta;
import i2f.core.match.regex.data.RegexMatchItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2023/8/31 9:38
 * @desc
 */
public class RegexUtil {
    public static List<RegexMatchItem> regexFinds(String str, String regex) {
        List<RegexMatchItem> ret = new ArrayList<>();
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(str);
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            RegexMatchItem item = new RegexMatchItem();
            item.srcStr = str;
            item.regexStr = regex;
            item.matchStr = matcher.group();
            item.idxStart = result.start();
            item.idxEnd = result.end();

            ret.add(item);
        }
        return ret;
    }

    /**
     * 将字符串分解为描述的有序对象，对象中包含匹配串或者非匹配串部分，为有序ArrayList
     * 以便根据此List分别对匹配和非匹配部分提取或者重新构造字符串
     *
     * @param str
     * @param regex
     * @return
     */
    public static List<RegexFindPartMeta> regexFindParts(String str, String regex) {
        List<RegexFindPartMeta> ret = new ArrayList<>(64);
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(str);
        int lidx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();

            RegexFindPartMeta oth = new RegexFindPartMeta();
            oth.part = str.substring(lidx, result.start());
            oth.isMatch = false;
            if (!"".equals(oth.part)) {
                ret.add(oth);
            }

            lidx = result.end();
            String group = matcher.group();

            RegexFindPartMeta mth = new RegexFindPartMeta();
            mth.part = group;
            mth.isMatch = true;
            ret.add(mth);

        }

        RegexFindPartMeta oth = new RegexFindPartMeta();
        oth.part = str.substring(lidx);
        oth.isMatch = false;
        if (!"".equals(oth.part)) {
            ret.add(oth);
        }

        return ret;
    }

    /**
     * 对匹配到的内容进行使用Map进行转换后重新恢复完整串
     * 例如：
     * str=a1b2c3
     * regex=\d+
     * mapper=(str)->String.format("0x%x",Integer.parseInf(str))
     * 则返回值为
     * ret=a0x1b0x2c0x3
     *
     * @param str
     * @param regex
     * @param mapper
     * @return
     */
    public static String regexFindAndReplace(String str, String regex, Function<String, String> mapper) {
        List<RegexFindPartMeta> list = regexFindParts(str, regex);
        StringBuilder builder = new StringBuilder();
        for (RegexFindPartMeta item : list) {
            if (!item.isMatch) {
                builder.append(item.part);
            } else {
                builder.append(mapper.apply(item.part));
            }
        }
        return builder.toString();
    }

}
