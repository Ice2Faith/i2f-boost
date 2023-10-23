package i2f.springboot.cors;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Ice2Faith
 * @date 2023/10/23 17:46
 * @desc
 */
public class OriginPattenCorsConfiguration extends CorsConfiguration {
    private static AntPathMatcher MATCHER = new AntPathMatcher("|");

    @Override
    public String checkOrigin(String requestOrigin) {
        String ret = super.checkOrigin(requestOrigin);
        if (ret == null) {
            Iterator<String> iterator = Optional.ofNullable(getAllowedOrigins())
                    .orElseGet(ArrayList::new)
                    .iterator();
            while (iterator.hasNext()) {
                String patten = iterator.next();
                if (MATCHER.match(patten, requestOrigin)) {
                    return requestOrigin;
                }
            }
        }
        return null;
    }
}
