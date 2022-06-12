package i2f.springcloud.spring.gateway.predicates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/6/12 16:25
 * @desc 判断Header/Query中需要包含指定名称的参数或者请求头
 * 如果正则不为空，则还需要满足正则
 * - RequestAttr=tokenType,\d+
 * 也就是判定Header里面的tokenType这个请求头是否是一个数值
 */
@ConditionalOnExpression("${i2f.springcloud.config.gateway.predicates.request-attr.enable:true}")
@Component
@Slf4j
public class RequestAttrRoutePredicateFactory extends AbstractRoutePredicateFactory<RequestAttrRoutePredicateFactory.Config> implements InitializingBean {

    public RequestAttrRoutePredicateFactory(){
        super(Config.class);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RequestAttrRoutePredicateFactory config done.");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                String paramName= config.getParam();
                String paramValue = exchange.getRequest().getHeaders().getFirst(paramName);
                if(paramValue==null){
                    paramValue=exchange.getRequest().getQueryParams().getFirst(paramName);
                }
                if(paramValue==null){
                    return false;
                }
                String regexp= config.getRegexp();
                if(regexp!=null && !"".equals(regexp)){
                    return paramValue.matches(regexp);
                }
                return true;
            }
        };
    }

    // 绑定配置参数名称
    @Override
    public List<String> shortcutFieldOrder() {
        // 也就是按照断言配置中的参数进行按照这个顺序映射到配置bean中
        // 例如本实例的配置
        // - RequestAttr=tokenType,1|2|5
        // 则映射到配置类中就为：param=tokenType,regexp=1|2|5
        return Arrays.asList("param","regexp");
    }

    // 接受配置文件中的配置参数
    public static class Config{
        // 配置类中的属性名，需要通过shortcutFieldOrder指定参数绑定名称
        private String param;
        private String regexp;

        public String getParam() {
            return param;
        }

        public Config setParam(String param) {
            this.param = param;
            return this;
        }

        public String getRegexp() {
            return regexp;
        }

        public Config setRegexp(String regexp) {
            this.regexp = regexp;
            return this;
        }
    }
}
