package i2f.springcloud.netflix.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.zuul.response-charset-filter.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.zuul.response-charset-filter")
@Component
public class ZuulResponseCharsetFilter extends ZuulFilter {
    private String charset ="UTF-8";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx= RequestContext.getCurrentContext();
        HttpServletRequest request= ctx.getRequest();
        HttpServletResponse response= ctx.getResponse();

        response.setCharacterEncoding(charset);

        return null;
    }
}
