package com.david.message.solution.consumer;

import com.alibaba.fastjson.JSON;
import com.david.message.solution.retry.AbstractMessageRetry;
import com.david.message.solution.common.ConsumeMessage;
import com.david.message.solution.domian.DeviceAlarm;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 消息消费者 手动确认
 */
@Component
public class ConsumerMessage {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AbstractMessageRetry consumerMessageRetry;

    @RabbitListener(queues = "solutionTopic")
    public void receiveMessage(Channel channel, Message message) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String data = new String(message.getBody());
           DeviceAlarm deviceAlarm = JSON.parseObject(data,DeviceAlarm.class);
           if(deviceAlarm.getId().equals("QW736FGD")){
               //TODO 直接加入死信队列
               ConsumeMessage.rejectMessageAndFormDeadQueue(channel,deliveryTag);
           }else if(deviceAlarm.getId().equals("GG536D")){
               logger.info("设备告警ID："+ deviceAlarm.getId());
               Thread.sleep(2000);
               //TODO 重新消费10次，还没有成功加入死信队列
               consumerMessageRetry.retryConsumeAfterOtherThing(channel,data,deliveryTag,10);
               ConsumeMessage.nackOneMessageAgainConsume(channel,deliveryTag);
           }else{
               ConsumeMessage.messageConsumeOneSuccess(channel,deliveryTag);
           }
       }catch (Exception e){
         logger.error(MessageFormat.format("消费消息出现错误：{0}",message.getMessageProperties().getRedelivered()));
         if(message.getMessageProperties().getRedelivered()){ //触发条件：rabbitmq停止运行
             System.out.println("消息已重复处理失败,拒绝再次接收！");
             ConsumeMessage.rejectMessageAndFormDeadQueue(channel,deliveryTag);
           }else{
             System.out.println("消息即将再次返回队列处理！");
             ConsumeMessage.nackOneMessageAgainConsume(channel,deliveryTag);
         }

       }
    }


}
