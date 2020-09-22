package com.luna.demo.controller;

import com.luna.demo.common.MqDto;
import com.luna.demo.common.OrderDto;
import com.luna.demo.config.RabbitMQExchangeConfig;
import com.luna.demo.service.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mq")
@Slf4j
public class MqController {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private RabbitMQService rabbitMQService;


    /**
     * 创建订单
     * @return
     */
    @GetMapping("createOrder")
    public String createOrder(String title){
        OrderDto orderDto=new OrderDto();
        orderDto.setOrderNo(RandomStringUtils.randomAlphanumeric(10));
        orderDto.setTitle(title);
        orderDto.setBody("黑色的小米手机");
        MqDto<OrderDto> mqDto=new MqDto<>();
        mqDto.setBusChannel("order");
        mqDto.setExchange(RabbitMQExchangeConfig.TOPIC_EXCHANGE);
        mqDto.setRoutingKey("order.ttl");
        mqDto.setExpire(10000L);
        mqDto.setMessageId(orderDto.getOrderNo());
        mqDto.setBody(orderDto);
        rabbitMQService.sendTTl(mqDto);
        log.info("send ok");
        return "ok";
    }




    @GetMapping("noExchange")
    public String noExchange(String msg) {
        //模拟confirm
        rabbitTemplate.convertAndSend("hello", "hello_queuq_key", msg);
        //模拟return
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.DIRECT_EXCHANGE, "hello_queuq_key", msg);

        return "ok";
    }

    @GetMapping("direct")
    public String direct(String direct) {
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.DIRECT_EXCHANGE, "direct_queue_key", direct);
        return "ok";
    }


    @GetMapping("topic")
    public String topic(String topic) {
        //* 单个匹配
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.TOPIC_EXCHANGE, "order.queue.key", topic);
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.TOPIC_EXCHANGE, "bill.queue.key", topic);
        //# 多个匹配
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.TOPIC_EXCHANGE, "order.1.queue.key", topic);
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.TOPIC_EXCHANGE, "bill.1.queue.key", topic);
        return "ok";
    }


    @GetMapping("fanout")
    public String fanout(String fanout) {
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.FANOUT_EXCHANGE, "", fanout);
        return "ok";
    }
}
