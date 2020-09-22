package com.luna.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderTtlQueueConfig {

    public static final String TTL_ORDER_QUEUE="ttl_order_queue";

    public static final String TTL_ROUT_KEY="#.ttl";

    /**
     * ttlqueue 里的消息过期之后转移到死信队列中
     * @return
     */
    @Bean("ttlOrderQueue")
    public Queue ttlOrderQueue(){
        Map<String,Object> params=new HashMap<>();
        params.put(DlxConstant.DEAD_LETTER_EXCHANGE,RabbitMQExchangeConfig.DIRECT_EXCHANGE);
        params.put(DlxConstant.DEAD_LETTER_QUEUE_KEY,DlxConfig.DLX_TTL_ROUT_KEY);
        return new Queue(TTL_ORDER_QUEUE,true,false,false,params);
    }

    @Bean("ttlOrderBind")
    public Binding ttlOrderBind(@Autowired @Qualifier("topicExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(ttlOrderQueue()).to(topicExchange).with(TTL_ROUT_KEY);
    }
}
