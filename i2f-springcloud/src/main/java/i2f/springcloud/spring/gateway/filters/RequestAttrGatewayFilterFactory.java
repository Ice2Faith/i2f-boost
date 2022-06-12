package i2f.springcloud.spring.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ltb
 * @date 2022/6/12 17:43
 * @desc 检查header/query中存在指定的参数才放行，否则直接402 PAYMENT_REQUIRED
 */
@ConditionalOnExpression("${i2f.springcloud.config.gateway.filters.request-attr.enable:true}")
@Component
@Slf4j
public class RequestAttrGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                String name = config.getName();
                String value=request.getHeaders().getFirst(name);
                if(value==null){
                    value=request.getQueryParams().getFirst(name);
                }
                if(value==null){
                    // 设置402并处理完成，直接返回
                    response.setStatusCode(HttpStatus.PAYMENT_REQUIRED);
                    response.setComplete();
                }
                String regex=config.getValue();
                if(regex!=null && !"".equals(regex)){
                    if(!value.matches(regex)){
                        response.setStatusCode(HttpStatus.PAYMENT_REQUIRED);
                        response.setComplete();
                    }
                }
                return chain.filter(exchange);
            }
        };
    }
}
