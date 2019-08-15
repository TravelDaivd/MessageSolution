package com.david.message.solution.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.ChannelN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 消息消费者 手动确认
 */
//@Component
public class ConsumerMQ {
    Logger logger = LoggerFactory.getLogger(this.getClass());

   // @RabbitListener(queues = "solutionTopic")
    public void receiveMessage(Channel channel, Message message) throws IOException {
        logger.info("消费消息："+ new String (message.getBody()));
        logger.info("DeliveryTag消息："+ message.getMessageProperties().getDeliveryTag());
       try {
           Thread.sleep(10000);
           //确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
           channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
       }catch (Exception e){
         logger.error(MessageFormat.format("消费消息出现错误：{0}",message.getMessageProperties().getRedelivered()));
         if(message.getMessageProperties().getRedelivered()){ //触发条件：rabbitmq停止运行
             System.out.println("消息已重复处理失败,拒绝再次接收！");
             // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
             channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
           }else{
             System.out.println("消息即将再次返回队列处理！");
             // requeue为是否重新回到队列，true重新入队
             channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
         }

       }
    }


}
