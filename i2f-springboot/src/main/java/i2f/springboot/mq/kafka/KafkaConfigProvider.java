package i2f.springboot.mq.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/6/13 9:43
 * @desc
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.springboot.config.kafka")
@Configuration
public class KafkaConfigProvider implements EnvironmentAware {

    private Environment env;

    private String serverAddress;

    private int retries=3;
    private String acks="all";
    private long maxBlockMs=6000;
    private long batchSize=4096;
    private long lingerMs=1000;
    private long bufferMemorySize=33554432;
    private long maxRequestSize=1048576;
    private String clientId; // 默认spring.application.name
    private String keySerializerClass= StringSerializer.class.getName();
    private String valueSerializerClass=StringSerializer.class.getName();
    private String compressionType="gzip";
    private String partitionerClass; // 默认无

    private boolean enableAutoCommit=false;
    private long autoCommitIntervalMs=1000;
    private long maxPollRecords=100;
    private String groupId; // 默认spring.application.name
    private long sessionTimeoutMs=120000;
    private long requestTimeoutMs=120000;
    private String keyDeserializerClass=StringDeserializer.class.getName();
    private String valueDeserializerClass=StringDeserializer.class.getName();
    private String autoOffsetReset="latest";
    private String interceptorClass; // 默认无


    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }


    // 配置生产工厂
    public Map<String, Object> producerConfig() {
        if(clientId==null || "".equals(clientId)){
            clientId=env.getProperty("spring.application.name");
        }

        Map<String, Object> props = new HashMap<>();
        //kafka 集群地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        //重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        //应答级别
        //acks=0 把消息发送到kafka就认为发送成功
        //acks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
        //acks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        //KafkaProducer.send() 和 partitionsFor() 方法的最长阻塞时间 单位 ms
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
        //批量处理的最大大小 单位 byte
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        //发送延时,当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        //生产者可用缓冲区的最大值 单位 byte
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemorySize);
        //每条消息最大的大小
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        //客户端ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        //Key 序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        //Value 序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
        //消息压缩：none、lz4、gzip、snappy，默认为 none。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        if(partitionerClass!=null && !"".equals(partitionerClass)){
            //自定义分区器
            props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, partitionerClass);
        }
        return props;
    }

    public Map<String, Object> consumerConfig() {
        if(groupId==null || "".equals(groupId)){
            groupId=env.getProperty("spring.application.name");
        }

        Map<String, Object> props = new HashMap<>();
        //kafka集群地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        //自动提交 offset 默认 true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        //自动提交的频率 单位 ms
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        //批量消费最大数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        //消费者组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //session超时，超过这个时间consumer没有发送心跳,就会触发rebalance操作
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        //请求超时
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        //Key 反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        //Value 反序列化类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);
        //当kafka中没有初始offset或offset超出范围时将自动重置offset
        //earliest:重置为分区中最小的offset
        //latest:重置为分区中最新的offset(消费分区中新产生的数据)
        //none:只要有一个分区不存在已提交的offset,就抛出异常
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        //设置Consumer拦截器
        if(interceptorClass!=null && !"".equals(interceptorClass)) {
            props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptorClass);
        }
        return props;
    }
}
