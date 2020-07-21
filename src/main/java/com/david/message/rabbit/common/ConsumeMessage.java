package com.david.message.rabbit.common;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理消费消息
 * @author gulei
 */
public class ConsumeMessage {
    private static Logger logger = LoggerFactory.getLogger(ConsumeMessage.class);

    /**
     * 消费端消费成功一条消息
     * @param channel
     * @param deliveryTag
     */
    public static void messageConsumeOneSuccess(Channel channel,long deliveryTag ){
        try {

            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            logger.error("消息一条消费成功出错："+e.getMessage());
        }
    }

    /**
     * 消费端消费成功一条消息 [这个慎用]
     * @param channel
     * @param deliveryTag
     */
    public static void messagConsumeAllSuccess(Channel channel,long deliveryTag ){
        try {
            channel.basicAck(deliveryTag,true);
        }catch (Exception e){
            logger.error("消息所有消费成功出错："+e.getMessage());
        }
    }

    /**
     * 拒绝消费消息，把消息加入到死信队列
     * @param channel
     * @param deliveryTag
     */
    public static void rejectMessageAndFormDeadQueue(Channel channel,long deliveryTag ){
        try {
            channel.basicReject(deliveryTag, false);
        }catch (Exception e){
            logger.error("拒绝消费消息加入死信队列出错："+e.getMessage());
        }
    }
    /**
     * 拒绝消费消息，重新入队消费
     * @param channel
     * @param deliveryTag
     */
    public static void rejectMessageAndAgainConsume(Channel channel,long deliveryTag){
        try {
            channel.basicReject(deliveryTag, true);
        }catch (Exception e){
            logger.error("拒绝消费消息重新入队消费出错："+e.getMessage());
        }
    }

    /**
     *   一条消息重新入队消费
     * @param channel
     * @param deliveryTag
     */
    public static void nackOneMessageAgainConsume(Channel channel,long deliveryTag ){
        try {
            channel.basicNack(deliveryTag, false, true);
        }catch (Exception e){
            logger.error("重新消息一条消费出错："+e.getMessage());
        }
    }
    /**
     *  一条消息拒绝消费，加入死信队列
     * @param channel
     * @param deliveryTag
     */
    public static void nackOneMessageConsumeJoinDeadQueue(Channel channel,long deliveryTag ){
        try {
            channel.basicNack(deliveryTag, false, false);
        }catch (Exception e){
            logger.error("重新消息一条消费加入死信队列出错："+e.getMessage());
        }
    }

    /**
     *  所有消息重新入队消费
     * @param channel
     * @param deliveryTag
     */
    public static void nackAllMessageAgainConsume(Channel channel,long deliveryTag ){
        try {
            channel.basicNack(deliveryTag, true, true);
        }catch (Exception e){
            logger.error("重新消息所有消费出错："+e.getMessage());
        }
    }
    /**
     * 所有消息拒绝消费，加入死信队列
     * @param channel
     * @param deliveryTag
     */
    public static void nackAllMessageJoinDeadQueue(Channel channel,long deliveryTag ){
        try {
            channel.basicNack(deliveryTag, true, false);
        }catch (Exception e){
            logger.error("重新消息所有消费加入死信队列出错："+e.getMessage());
        }
    }
}
