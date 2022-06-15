package i2f.springboot.mq.rabbit.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/6/15 10:09
 * @desc
 */
@Slf4j
@Component
@ConditionalOnMissingBean(RabbitTemplate.ReturnCallback.class)
public class RabbitReturnCallbackLogImpl implements RabbitTemplate.ReturnCallback{
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("ReturnCallback:     " + "消息：" + message);
        log.info("ReturnCallback:     " + "回应码：" + replyCode);
        log.info("ReturnCallback:     " + "回应信息：" + replyText);
        log.info("ReturnCallback:     " + "交换机：" + exchange);
        log.info("ReturnCallback:     " + "路由键：" + routingKey);
    }
}
