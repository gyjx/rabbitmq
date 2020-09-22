package com.luna.demo.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQExchangeConfig {


    /**
     * direct sexchange 点对点  完全匹配
     */
    public static final String DIRECT_EXCHANGE = "direct_exchange";

    /**
     * topic_exchage 点对点 规则匹配
     */
    public static final String TOPIC_EXCHANGE = "topic_exchange";

    /**
     * fanout_exchage 广播
     */
    public static final String FANOUT_EXCHANGE = "fanout_exchange";


    @Bean("directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

}