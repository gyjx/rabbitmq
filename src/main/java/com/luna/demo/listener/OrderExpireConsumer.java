package com.luna.demo.listener;

import com.luna.demo.common.DefaultConsumer;
import com.luna.demo.common.MqDto;
import com.luna.demo.common.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderExpireConsumer extends DefaultConsumer<MqDto<OrderDto>> {

    @Autowired
    RabbitProperties rabbitProperties;

    @Override
    public RabbitProperties getRabbitProperties() {
        return rabbitProperties;
    }

    /**
     * 具体的业务逻辑
     * @param orderDtoMqDto
     */
    @Override
    public void execute(MqDto<OrderDto> orderDtoMqDto) {
        //模拟订单过期消费
        log.info("OrderExpireConsumer,orderDto:{}", orderDtoMqDto.toString());
    }
}
