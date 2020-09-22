package com.luna.demo.listener;

import com.luna.demo.common.MqDto;
import com.luna.demo.common.OrderDto;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQListener {

    @Autowired
    private OrderExpireConsumer orderExpireConsumer;

    /**
     * 监听订单死信队列
     *
     * @param channel
     * @param mqDto
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = {"#{dlxTtlQueue.name}"})
    public void orderDlxTtl(@Header(AmqpHeaders.CHANNEL) Channel channel, MqDto<OrderDto> mqDto, Message message) throws Exception {
        orderExpireConsumer.run(channel, message, mqDto);
    }


    @RabbitListener(queues = {"#{directQueue.name}"})
    public void directQueue(@Header(AmqpHeaders.CHANNEL) Channel channel, String msg, Message message) throws Exception {

        log.info("msg:{},mq.message:{}", msg, message.toString());

        //手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


    @RabbitListener(queues = {"#{topicQueue.name}"})
    public void topicQueue(@Header(AmqpHeaders.CHANNEL) Channel channel, String msg, Message message) throws Exception {

        log.info("topicQueue,msg:{},mq.message:{}", msg, message.toString());

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


    @RabbitListener(queues = {"#{topic2Queue.name}"})
    public void topic2Queue(@Header(AmqpHeaders.CHANNEL) Channel channel, String msg, Message message) throws Exception {

        log.info("topic2Queue,msg:{},mq.message:{}", msg, message.toString());

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


    @RabbitListener(queues = {"#{fanoutQueue.name}"})
    public void fanoutQueue(@Header(AmqpHeaders.CHANNEL) Channel channel, String msg, Message message) throws Exception {

        log.info("msg:{},mq.message:{}", msg, message.toString());

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


}
