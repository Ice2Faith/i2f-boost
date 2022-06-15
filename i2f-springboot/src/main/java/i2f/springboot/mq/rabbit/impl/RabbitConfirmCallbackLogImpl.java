package i2f.springboot.mq.rabbit.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/6/15 10:06
 * @desc
 */
@Slf4j
@Component
@ConditionalOnMissingBean(RabbitTemplate.ConfirmCallback.class)
public class RabbitConfirmCallbackLogImpl implements RabbitTemplate.ConfirmCallback{
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
        log.info("ConfirmCallback:     " + "确认情况：" + ack);
        log.info("ConfirmCallback:     " + "原因：" + cause);
    }
}
