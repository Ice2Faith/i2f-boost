package i2f.springcloud.spring.gateway.filters.global;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * @author ltb
 * @date 2022/6/12 17:57
 * @desc
 */
@ConditionalOnExpression("${springcloud.config.gateway.global.filters.request-log.enable:true}")
@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "springcloud.config.gateway.global.filters.request-log")
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    private boolean showHeaders=false;

    private boolean showQuerys=true;

    public static final String EXCHANGE_BEGIN_TIME_KEY="exchangeBeginTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        exchange.getAttributes().put(EXCHANGE_BEGIN_TIME_KEY,System.currentTimeMillis());

        request.getHeaders().add("trace-id", UUID.randomUUID().toString());

        RequestPath path = request.getPath();
        StringBuilder builder=new StringBuilder();
        builder.append("\n-----------global filter begin------------").append("\n");
        builder.append("request path:"+path).append("\n");

        builder.append("request method:"+request.getMethod().name()).append("\n");
        if(showHeaders){
            builder.append("request header:").append("\n");
            HttpHeaders headers = request.getHeaders();
            for(String item : headers.keySet()){
                List<String> vals=headers.get(item);
                for(String val : vals){
                    builder.append("\t"+item+":"+val).append("\n");
                }
            }
        }

        if(showQuerys){
            builder.append("request query:").append("\n");
            MultiValueMap<String, String> query = request.getQueryParams();
            for(String item : query.keySet()){
                List<String> vals=query.get(item);
                for(String val : vals){
                    builder.append("\t"+item+":"+val).append("\n");
                }
            }
        }

        log.info(builder.toString()+"-----------global filter pending ------------\n");
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            Long beginTime=exchange.getAttribute(EXCHANGE_BEGIN_TIME_KEY);
            if(beginTime==null){
                return;
            }
            builder.append("process time:\t"+(System.currentTimeMillis()-beginTime)+"ms").append("\n");
            builder.append("-----------global filter end------------").append("\n");

            log.info(builder.toString());
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
