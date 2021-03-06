package com.david.message.rabbit.common;


import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 定义发送消息： 返回回调，ack机制回调，操作回调
 * @author gulei
 */

public interface PushMessage extends RabbitTemplate.ReturnCallback,RabbitTemplate.ConfirmCallback{

    /**
     * 普通方式发送消息，没有ack
     * @param message   消息
     * @param queueName  队列
     */
    void ordinarySendMessage(Object message,String queueName);

    /**
     * 根据队列，发送消息
     * 支持确认机制ACK和发送失败回退机制
     * @param message       消息
     * @param queueName   队列名称
     * @param IGenId  这里设置的ID可以是业务数据ID也可以是随机UUID
     */
    void sendMessageAndCallback(Object message,String queueName, String IGenId);


    /**
     * 根据交换器和路由键发送消息
     *支持确认机制ACK和发送失败回退机制
     * @param message        要发送的消息
     * @param exchangeName    交换器
     * @param routingKey       路由键
     * @param IGenId   这里设置的ID可以是业务数据ID也可以是随机UUID
     */
    void sendMessageAndCallback(Object message,String exchangeName,String routingKey, String IGenId);

    /**
     * 普通方式发送消息，没有ack 但有消息过期时间
     * @param message   消息
     * @param queueName  队列
     * @param second  过期时间 (秒)
     */
    void ordinarySendMessage(Object message,String queueName,int second);
    /**
     * 根据队列，发送消息
     * 支持确认机制ACK、消息过期时间和发送失败回退机制
     * @param message       消息
     * @param queueName   队列名称
     * @param IGenId  这里设置的ID可以是业务数据ID也可以是随机UUID
     * @param second  消息过期时间 (秒)
     */
    void sendMessageAndCallback(Object message,String queueName, String IGenId,int second);
    /**
     * 根据交换器和路由键发送消息
     *支持确认机制ACK、消息过期时间和发送失败回退机制
     * @param message        要发送的消息
     * @param exchangeName    交换器
     * @param routingKey       路由键
     * @param IGenId   这里设置的ID可以是业务数据ID也可以是随机UUID
     * @param second  过期时间 (秒)
     */
    void sendMessageAndCallback(Object message,String exchangeName,String routingKey, String IGenId,int second);

}
