package com.david.message.solution.common;

import com.david.message.solution.common.exchange.RabbitMQExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitServer implements PushMessage, InitializingBean {

    Logger logger = LoggerFactory.getLogger(RabbitServer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AmqpAdmin amqpAdmin;

    private RabbitCallback rabbitCallback;

    @Override
    public void ordinarySendMessage(Object message, String queueName) {
        logger.info("ordinarySendMessage > "+ message);
        rabbitTemplate.convertAndSend(queueName,message);
    }

    @Override
    public void ordinarySendMessage(Object message, String queueName,int second) {
        logger.info("ordinarySendMessage > "+ message);
        MessagePostProcessor messagePostProcessor = setMessageExpirationTime(second);
        rabbitTemplate.convertAndSend(queueName,message,messagePostProcessor);
    }

    @Override
    public void sendMessageAndCallback(Object message, String queueName,String IGenId) {
        logger.info("sendMessageAndCallback > "+ message);
        CorrelationData correlationData = new CorrelationData(IGenId);
        rabbitTemplate.convertAndSend(queueName,message,correlationData);
    }

    @Override
    public void sendMessageAndCallback(Object content, String queueName, String IGenId, int second) {
        logger.info("sendMessageAndCallback > "+ content);
        CorrelationData correlationData = new CorrelationData(IGenId);
        MessagePostProcessor messagePostProcessor = setMessageExpirationTime(second);
        rabbitTemplate.convertAndSend(queueName,content,messagePostProcessor,correlationData);
    }

    @Override
    public void sendMessageAndCallback(Object message, String exchangeName, String routingKey, String IGenId) {
        logger.info("sendMessageAndCallback > "+ message);
        CorrelationData correlationData = new CorrelationData(IGenId);
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message,correlationData);
    }

    @Override
    public void sendMessageAndCallback(Object content, String exchangeName, String routingKey, String IGenId, int second) {
        logger.info("sendMessageAndCallback > "+ content);
        CorrelationData correlationData = new CorrelationData(IGenId);
        MessagePostProcessor messagePostProcessor = setMessageExpirationTime(second);
        rabbitTemplate.convertAndSend(exchangeName,routingKey,content,messagePostProcessor,correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String value) {
        String id = correlationData.getId();
        if(ack){
            // 队列名称或是路由键写错都会触发此流程
            logger.info("消息发送端确认成功，msgid={},value={}",id,value);
            if(rabbitCallback != null){
                rabbitCallback.messageSendSuccess(id);
            }
        }else{
            //触发此流程前置条件是 exchange不存在（交换器名称写错）情况
            logger.error("消息发送端确认失败 msgid={} ",id);
            if(rabbitCallback != null){
                rabbitCallback.messageSendFail(id);
            }
        }
    }

    /**
     * 当队列名称或是路由键写错或是不存在会触发此流程
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String sendFailMessage  = new String(message.getBody());
        logger.error("消息： {} 发送失败，应答码：{}，原因：{}，交换机：{}，路由键：{}",sendFailMessage,replyCode,replyText,exchange,routingKey);
        rabbitCallback.messageSendFailReturn(message,replyCode,replyText,exchange,routingKey);
    }

    @Override
    public void afterPropertiesSet() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }

    public void setRabbitExchangeQueue(RabbitMQExchange rabbitMQExchange) {
        rabbitMQExchange.bindingExchangeAndQueue(amqpAdmin);
    }

    public void setRabbitCallback(RabbitCallback rabbitCallback) {
        this.rabbitCallback = rabbitCallback;
    }

    /**
     * 设置消息过期时间
     * @param second
     * @return
     */
    private  MessagePostProcessor setMessageExpirationTime(int second){
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setContentEncoding("utf-8");
            messageProperties.setExpiration(String.valueOf(second*1000));
            return message;
        };
        return messagePostProcessor;
    }

}
