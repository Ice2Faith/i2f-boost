package i2f.spring.filter;


import i2f.core.lang.functional.common.IGetter;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/10/27 9:47
 * @desc
 */
public class WhiteFilter {

    public static AntPathMatcher urlMatcher = new AntPathMatcher("/");
    public static AntPathMatcher pkgMatcher = new AntPathMatcher(".");

    public static <IN extends Collection<E>, OUT extends Collection<E>, E> OUT antUrlFilter(OUT out, IN in, Collection<String> includes, Collection<String> excludes, IGetter<String, E> getter) {
        return filter(out, in, includes, excludes, (elem, patten) -> {
            return urlMatcher.match(patten, getter.get(elem));
        });
    }

    public static <IN extends Collection<E>, OUT extends Collection<E>, E> OUT antPkgFilter(OUT out, IN in, Collection<String> includes, Collection<String> excludes, IGetter<String, E> getter) {
        return filter(out, in, includes, excludes, (elem, patten) -> {
            return pkgMatcher.match(patten, getter.get(elem));
        });
    }

    public static <IN extends Collection<E>, OUT extends Collection<E>, E, P> OUT filter(OUT out, IN in, Collection<P> includes, Collection<P> excludes, IMatcher<E, P> matcher) {
        boolean excluded = false;
        boolean included = false;
        if (excludes != null && excludes.size() > 0) {
            excluded = true;
        }
        if (includes != null && includes.size() > 0) {
            included = true;
        }
        if (!excluded && !included) {
            out.addAll(in);
            return out;
        }
        for (E item : in) {
            if (excluded) {
                boolean keep = true;
                for (P exp : excludes) {
                    if (matcher.match(item, exp)) {
                        keep = false;
                        break;
                    }
                }
                if (!keep) {
                    continue;
                }
            }
            if (included) {
                boolean keep = false;
                for (P inp : includes) {
                    if (matcher.match(item, inp)) {
                        keep = true;
                        break;
                    }
                }
                if (!keep) {
                    continue;
                }
            }

            out.add(item);
        }

        return out;
    }
}
