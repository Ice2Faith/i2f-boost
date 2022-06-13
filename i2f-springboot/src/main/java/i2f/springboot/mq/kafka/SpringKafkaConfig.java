package i2f.springboot.mq.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/6/13 9:43
 * @desc
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.springboot.config.spring-kafka.enable:true}")
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.spring-kafka")
public class SpringKafkaConfig {

    private String txPrefix="tx";
    private boolean batchListener=false;
    private int concurrency=1;

    @Autowired
    private KafkaConfigProvider configProvider;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configProvider.getServerAddress());
        return new KafkaAdmin(props);
    }

    @Bean
    @Primary
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(configProvider.producerConfig());
    }

    @Bean
    public ProducerFactory<String, String> producerFactoryWithTransaction() {
        DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory = (DefaultKafkaProducerFactory<String, String>) producerFactory();
        //设置事务Id前缀
        defaultKafkaProducerFactory.setTransactionIdPrefix(txPrefix);
        return defaultKafkaProducerFactory;
    }

    @Bean
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateWithTransaction() {
        return new KafkaTemplate<>(producerFactoryWithTransaction());
    }


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(configProvider.consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //设置 consumerFactory
        factory.setConsumerFactory(consumerFactory());
        //设置是否开启批量监听
        factory.setBatchListener(batchListener);
        //设置消费者组中的线程数量
        factory.setConcurrency(concurrency);
        return factory;
    }

    @Bean
    @ConditionalOnExpression("${i2f.springboot.config.spring-kafka.consumer-error-log-listener.enable:true}")
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
                //打印消费异常的消息和异常信息
                log.error("consumer failed! message: {}, exceptionMsg: {}, groupId: {}", message, exception.getMessage(), exception.getGroupId());
                return null;
            }
        };
    }
}
