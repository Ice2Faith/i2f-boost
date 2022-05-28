package i2f.springcloud.alibaba.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/28 19:40
 * @desc
 */
@ConditionalOnBean(NacosConfig.class)
@ConditionalOnExpression("${i2f.springcloud.config.nacos.lb-nacos.enable:true}")
@Configuration
@EnableDiscoveryClient
public class NacosRuleConfig {

    /**
     * 配置自定义负载均衡策略时，
     * 对BeanName是有要求的
     * 必须是iRule
     * @return
     */
    @Bean
    public IRule iRule(){
        return new NacosRule();
    }
}
