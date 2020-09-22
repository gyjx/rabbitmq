package com.luna.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 设置返回回调和确认回调
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,RabbitMQConfirmAndReturn rabbitMQConfirmAndReturn) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setConfirmCallback(rabbitMQConfirmAndReturn);
        rabbitTemplate.setReturnCallback(rabbitMQConfirmAndReturn);
        //Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者，为false时匹配不到会直接被丢弃
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


    /********************direct************************/
    @Bean("directQueue")
    public Queue directQueue() {
        return new Queue("direct_queue", true, false, false);
    }

    @Bean("directBind")
    public Binding directBind(@Autowired @Qualifier("directExchange") DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue()).to(directExchange).with("direct_queue_key");
    }

    /********************topic1,************************/
    @Bean("topicQueue")
    public Queue topicQueue() {
        return new Queue("topic_queue", true, false, false);
    }
    /**
     *  * 是单个匹配
     * @param topicExchange
     * @return
     */
    @Bean("topicBind")
    public Binding topicBind(@Autowired @Qualifier("topicExchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue()).to(topicExchange).with("*.queue.key");
    }

    /********************topic2************************/
    @Bean("topic2Queue")
    public Queue topic2Queue() {
        return new Queue("topic_queue", true, false, false);
    }

    /**
     *  #可以多个匹配
     * @param topicExchange
     * @return
     */
    @Bean("topic2Bind")
    public Binding topic2Bind(@Autowired @Qualifier("topicExchange") TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue()).to(topicExchange).with("#.queue.key");
    }





    /********************fanout************************/
    @Bean("fanoutQueue")
    public Queue fanoutQueue() {
        return new Queue("fanout_queue", true, false, false);
    }

    @Bean("fanoutBind")
    public Binding fanoutBind(@Autowired @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange);
    }

}
