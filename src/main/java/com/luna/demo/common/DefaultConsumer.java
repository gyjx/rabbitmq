package com.luna.demo.common;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetrySynchronizationManager;

@Slf4j
public abstract class DefaultConsumer<T> {

    /**
     * 获取rabbitmq配置
     *
     * @return
     */
    public abstract RabbitProperties getRabbitProperties();

    /**
     * 业务执行
     *
     * @param t
     */
    public abstract void execute(T t) throws Exception;


    /**
     * 业务执行失败，处理策略
     */
    public void executeFail(Channel channel, Message message, Exception e) throws Exception {
        //获取当前重试的参数
        RetryContext context = RetrySynchronizationManager.getContext();
        //如果有异常，且没有达到最大投递次数,具体执行策略，这里直接ack
        if ((getRabbitProperties().getListener().getSimple().getRetry().getMaxAttempts() - 1 == context.getRetryCount())) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            throw e;
        }
    }

    public void run(Channel channel, Message message, T t) throws Exception {
        try {
            execute(t);
            //无异常，ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("consumer fail:{}", message.toString());
            executeFail(channel, message, e);
        }
    }

}