package i2f.spring.matcher;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2023/6/13 9:52
 * @desc
 */
public class MatcherUtil {
    private static AntPathMatcher antUrlMatcher = new AntPathMatcher("/");
    private static AntPathMatcher antPkgMatcher = new AntPathMatcher(".");

    public static boolean antUrlMatched(String str, String patten) {
        return antUrlMatcher.match(patten, str);
    }

    public static boolean antPkgMatched(String str, String patten) {
        return antPkgMatcher.match(patten, str);
    }

    public static boolean antUrlMatchedAny(String str, Collection<String> pattens) {
        for (String patten : pattens) {
            if (StringUtils.isEmpty(patten)) {
                continue;
            }
            if (antUrlMatched(str, patten)) {
                return true;
            }
        }
        return false;
    }

    public static boolean antPkgMatchedAny(String str, Collection<String> pattens) {
        for (String patten : pattens) {
            if (StringUtils.isEmpty(patten)) {
                continue;
            }
            if (antPkgMatched(str, patten)) {
                return true;
            }
        }
        return false;
    }
}
