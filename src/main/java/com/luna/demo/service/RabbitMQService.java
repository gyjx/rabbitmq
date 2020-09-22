package com.luna.demo.service;

import com.luna.demo.common.MqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQService {

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * rabbitmq 发送核心方法
     *
     * @param mqDto
     */
    public void send(MqDto mqDto) {
        rabbitTemplate.convertAndSend(mqDto.getExchange(), mqDto.getRoutingKey(), mqDto, message -> {
            message.getMessageProperties().setCorrelationId(mqDto.getMessageId());
            message.getMessageProperties().setMessageId(mqDto.getMessageId());
            return message;
        });
    }


    /**
     * rabbitmq 发送TTL消息
     *
     * @param mqDto
     */
    public void sendTTl(MqDto mqDto) {
        rabbitTemplate.convertAndSend(mqDto.getExchange(), mqDto.getRoutingKey(), mqDto, message -> {
            message.getMessageProperties().setExpiration(mqDto.getExpire() + "");
            message.getMessageProperties().setCorrelationId(mqDto.getMessageId());
            message.getMessageProperties().setMessageId(mqDto.getMessageId());
            return message;
        });
    }


}