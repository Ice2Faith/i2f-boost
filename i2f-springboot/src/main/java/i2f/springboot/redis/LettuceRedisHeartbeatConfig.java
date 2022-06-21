package i2f.springboot.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/6/21 14:10
 * @desc 在某些情况下，使用lettuce客户端，将会存在15分钟的TCP连接重传超时时间
 * 期间redis将不可用，原因是客户端不知道连接被断开了，一直重传，直到超出TCP最大重传周期为止
 * 一般发生在一段时间不用redis之后，例如一晚上或者午休期间没人用的时候
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.springboot.config.redis.lettuce.heart-beat.enable:true}")
@ConditionalOnClass(LettuceConnectionFactory.class)
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.redis.lettuce.heart-beat")
public class LettuceRedisHeartbeatConfig implements InitializingBean, ApplicationListener<ContextClosedEvent>,Runnable{
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private ScheduledExecutorService scheduledExecutorService;

    private long initDelaySeconds=30;

    private long rateSeconds=30;

    public void heartBeat(){
        if(redisConnectionFactory instanceof LettuceConnectionFactory){
            LettuceConnectionFactory factory=(LettuceConnectionFactory)redisConnectionFactory;
            factory.validateConnection();
            log.debug("LettuceRedisHeartbeatConfig heart-beat.");
        }
    }

    public void closeBeatPool(){
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdownNow();
            log.info("LettuceRedisHeartbeatConfig heart-beat shutdown.");
        }
    }
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        closeBeatPool();
    }

    @Override
    public void run() {
        heartBeat();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(redisConnectionFactory instanceof LettuceConnectionFactory){
            LettuceConnectionFactory factory=(LettuceConnectionFactory)redisConnectionFactory;
            factory.setValidateConnection(true);
            log.info("LettuceRedisHeartbeatConfig config done.");
            closeBeatPool();
            scheduledExecutorService= Executors.newSingleThreadScheduledExecutor(
                    (Runnable r)->new Thread(r,"lettuce-redis-heartbeat")
            );
            scheduledExecutorService.scheduleAtFixedRate(this,initDelaySeconds,rateSeconds, TimeUnit.SECONDS);
        }
    }
}
