package com.luna.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DlxConfig {

    /*************************ttl dlx *************************************/
    public static final String DLX_TTL_QUEUE = "dlx_ttl_queue";

    public static final String DLX_TTL_ROUT_KEY = "dlx.ttl.key";

    @Bean("dlxTtlQueue")
    public Queue dlxTtlQueue() {
        return new Queue(DLX_TTL_QUEUE, true, false, false);
    }

    @Bean("dlxTtlBind")
    public Binding dlxTtlBind(@Autowired @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(dlxTtlQueue()).to((directExchange)).with(DLX_TTL_ROUT_KEY);
    }

}
