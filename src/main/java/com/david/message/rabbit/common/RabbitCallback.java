package com.david.message.rabbit.common;

import org.springframework.amqp.core.Message;

/**
 * 通用回调接口
 * 实现此接口定制自己的发送消息回调
 * @author gulei
 */
public interface RabbitCallback {

    /**
     * 消息发送成功后业务方面处理或是其他处理事件
     * 路由健不存在 routingKey,队列不存在都会触发此事件
     * @param id
     */
    void messageSendSuccess(String id);

    /**
     * 消息发送服务器确认机制
     * 触发条件：交换器不存在，
     * @param id
     */
    void messageSendFail(String id);


    /**
     * 消息发送失败进行回退
     * 触发条件：路由健不存在 routingKey,队列写错或是不存在
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    void messageSendFailReturn(Message message, int replyCode, String replyText, String exchange, String routingKey);
}
