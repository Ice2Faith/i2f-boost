package i2f.springcloud.spring.gateway.filters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/4/1 16:01
 * @desc
 */
@Slf4j
@Data
public abstract class AbsAuthTokenFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        boolean isWhite = isWhiteList(request, exchange);
        if (isWhite) {
            return chain.filter(exchange);
        }

        String token = getToken(request, exchange);

        boolean ok = validToken(token, request, response, exchange);

        if (!ok) {
            return onBadToken(token, request, response, exchange);
        }
        return chain.filter(exchange);
    }

    public int getPathPrefixCount() {
        return 1; // 一般使用 /sys/** 或者是其他的固定前缀层级的分发方式，这种方式的情况下，可以按照前缀层级数进行截取
    }

    public String getPath(ServerHttpRequest request, ServerWebExchange exchange) {
        return request.getPath().subPath(getPathPrefixCount() * 2).toString();
    }

    public Set<String> getWhiteList() {
        return new HashSet<>();
    }

    public boolean isWhiteList(ServerHttpRequest request, ServerWebExchange exchange) {
        String path = getPath(request, exchange);
        Set<String> whiteList = getWhiteList();
        if (whiteList != null && !whiteList.isEmpty()) {
            return testWhitePath(path, whiteList);
        }
        return false;
    }

    public boolean testWhitePath(String path, Set<String> whiteList) {
        return whiteList.contains(path);
    }

    public String getToken(ServerHttpRequest request, ServerWebExchange exchange) {
        String tokenName = getTokenName();
        String token = request.getHeaders().getFirst(tokenName);
        if (token == null || "".equals(token)) {
            if (allowParamToken()) {
                token = request.getQueryParams().getFirst(tokenName);
            }
        }
        return token;
    }

    public String getTokenName() {
        return "token";
    }

    public boolean allowParamToken() {
        return true;
    }

    public abstract boolean validToken(String token, ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange);

    public Mono<Void> onBadToken(String token, ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange) {
        return responseHtml(response, "authenticate failure!<br/>");
    }

    @Override
    public int getOrder() {
        return -1;
    }


    public Mono<Void> responseContent(ServerHttpResponse response, int httpCode, String contentType, byte[] data) {
        response.setRawStatusCode(httpCode);
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        return response.writeWith(Mono.just(buffer));
    }

    public Mono<Void> responseHtml(ServerHttpResponse response, String html) {
        return responseHtml(response, HttpStatus.UNAUTHORIZED.value(), html);
    }

    public Mono<Void> responseHtml(ServerHttpResponse response, int httpCode, String html) {
        try {
            return responseContent(response, httpCode,
                    MediaType.TEXT_HTML_VALUE + ";charset=utf-8",
                    html.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public Mono<Void> responseJson(ServerHttpResponse response, String json) {
        return responseJson(response, HttpStatus.UNAUTHORIZED.value(), json);
    }

    public Mono<Void> responseJson(ServerHttpResponse response, int httpCode, String json) {
        try {
            return responseContent(response, httpCode,
                    MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8",
                    json.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
