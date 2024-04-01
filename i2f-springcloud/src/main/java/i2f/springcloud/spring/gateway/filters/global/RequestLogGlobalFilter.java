package i2f.springcloud.spring.gateway.filters.global;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    private boolean showHeaders = false;

    private boolean showQuerys = false;

    private boolean showStatistic = false;

    public static final String EXCHANGE_BEGIN_TIME_KEY = "exchangeBeginTime";

    protected ConcurrentHashMap<String, TimeStatistic> pathTimeStatMap = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, TimeStatistic> ipTimeStatMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest().mutate().header("trace-id", new String[]{UUID.randomUUID().toString()}).build();


        exchange.getAttributes().put(EXCHANGE_BEGIN_TIME_KEY, System.currentTimeMillis());
        InetSocketAddress remote = request.getRemoteAddress();
        String addr = remote == null ? "unknown" : remote.getAddress().getHostAddress();

        String path = request.getPath().toString();


        StringBuilder builder = new StringBuilder();
        builder.append("\n-----------global filter begin------------").append("\n");
        builder.append("request path:" + path).append("\n");
        builder.append("request method:" + request.getMethod().name()).append("\n");
        builder.append("from addr:" + addr).append("\n");
        if (showHeaders) {
            builder.append("request header:").append("\n");
            HttpHeaders headers = request.getHeaders();
            for (String item : headers.keySet()) {
                List<String> vals = headers.get(item);
                for (String val : vals) {
                    builder.append("\t" + item + ":" + val).append("\n");
                }
            }
        }

        if (showQuerys) {
            builder.append("request query:").append("\n");
            MultiValueMap<String, String> query = request.getQueryParams();
            for (String item : query.keySet()) {
                List<String> vals = query.get(item);
                for (String val : vals) {
                    builder.append("\t" + item + ":" + val).append("\n");
                }
            }
        }

        log.info(builder.toString() + "-----------global filter pending ------------\n");
        return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
            Long beginTime = exchange.getAttribute(EXCHANGE_BEGIN_TIME_KEY);
            if (beginTime == null) {
                return;
            }
            long ts = System.currentTimeMillis();
            long diff = (ts - beginTime);
            builder.append("process time:\t" + diff + "ms").append("\n");
            if (showStatistic) {
                pathTimeStatMap.computeIfAbsent(path, (key) -> {
                    return new TimeStatistic();
                });
                pathTimeStatMap.get(path).add(diff);

                ipTimeStatMap.computeIfAbsent(addr, (key) -> {
                    return new TimeStatistic();
                });
                ipTimeStatMap.get(addr).add(diff);
                builder.append("path time:\t" + pathTimeStatMap.get(path).formatString()).append("\n");
                builder.append("addr time:\t" + ipTimeStatMap.get(addr).formatString()).append("\n");
            }
            builder.append("-----------global filter end------------").append("\n");

            log.info(builder.toString());
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
