package i2f.springcloud.spring.gateway;

import i2f.springcloud.spring.gateway.filters.RequestAttrGatewayFilterFactory;
import i2f.springcloud.spring.gateway.filters.global.RequestLogGlobalFilter;
import i2f.springcloud.spring.gateway.predicates.RequestAttrRoutePredicateFactory;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/5/28 17:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        GatewayConfig.class,
        RequestAttrRoutePredicateFactory.class,
        RequestAttrGatewayFilterFactory.class,
        RequestLogGlobalFilter.class,
        GatewayCorsConfig.class
})
public @interface EnableGatewayConfig {

}

