package com.luna.demo.common;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@ToString
public class MqDto<T> implements Serializable {

    /**
     * 消息ID，用于去重，全局唯一,为空则系统自动创建
     */
    private String messageId;

    /**
     * 消息体
     */
    private T body;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由KEY，广播模式可以不传路由KEY
     */
    private String routingKey="";

    /**
     * 消息业务
     */
    private String busChannel;

    /**
     * 过期，单位毫秒
     */
    private Long expire;


    public void setMessageId(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            this.messageId = RandomStringUtils.random(10);
        } else {
            this.messageId = messageId;
        }
    }

}