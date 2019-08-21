package com.david.message.solution.retry;

import com.alibaba.fastjson.JSON;
import com.david.message.solution.common.MessageStatus;
import com.david.message.solution.common.ProducerMessage;
import com.david.message.solution.common.SolutionUtil;
import com.david.message.solution.domian.DeviceAlarm;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 根据不同的业务，重写这个实现 MessageRetry
 */
@Component
public class AbstractMessageRetry implements MessageRetry {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void retryConsumeAfterDeadQueue(Channel channel,String message,long deliveryTag, int retryCount) {
        try{
            String hashMessage = SolutionUtil.toHash(message);
            if(!SolutionUtil.receiveMessageConcurrentHashMap.containsKey(hashMessage)){
                SolutionUtil.receiveMessageConcurrentHashMap.put(hashMessage,1);
            }else{
                Integer count = SolutionUtil.receiveMessageConcurrentHashMap.get(hashMessage);
                logger.info("重试消费次数："+count);
                if(count == retryCount){
                    // TODO: 加入死信队列
                    channel.basicReject(deliveryTag, false);
                    SolutionUtil.receiveMessageConcurrentHashMap.remove(hashMessage);
                }else{
                    SolutionUtil.receiveMessageConcurrentHashMap.put(hashMessage,count+1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void retryConsumeAfterOtherThing(Channel channel,String message,long deliveryTag, int retryCount) {
        String hashMessage = SolutionUtil.toHash(message);
        if(!SolutionUtil.receiveMessageConcurrentHashMap.containsKey(hashMessage)){
            SolutionUtil.receiveMessageConcurrentHashMap.put(hashMessage,1);
        }else{
            Integer count = SolutionUtil.receiveMessageConcurrentHashMap.get(hashMessage);
            logger.info("重试消费次数："+count);
            if(count == retryCount){
                // TODO:  业务上面的处理
                DeviceAlarm deviceAlarm = JSON.parseObject(message,DeviceAlarm.class);
                deviceAlarm.setOperationType(MessageStatus.getValue(201).getStatus());
                if(SolutionUtil.deviceAlarmConcurrentHashMap.containsKey(deviceAlarm.getId())){
                    SolutionUtil.deviceAlarmConcurrentHashMap.put(deviceAlarm.getId(),deviceAlarm);
                }
                // TODO:   丢弃这条消息：1、消费这条消息，让rabbitMQ服务器删除掉
                try {
                    channel.basicAck(deliveryTag,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SolutionUtil.receiveMessageConcurrentHashMap.remove(hashMessage);
            }else{
                SolutionUtil.receiveMessageConcurrentHashMap.put(hashMessage,count+1);
            }
        }
    }


    @Override
    public void retryPushAfterOtherThing(ProducerMessage producerMessage,String id, int retryCount) {
        if(!SolutionUtil.sendMessageConcurrentHashMap.containsKey(id)){
            SolutionUtil.sendMessageConcurrentHashMap.put(id,1);
            producerMessage.retrySendMessage(id);
        }else {
            Integer count = SolutionUtil.sendMessageConcurrentHashMap.get(id);
            logger.info("重试发送消息次数："+count);
            if(count == retryCount){
                // TODO:  业务上面的处理,然后删除内存数据
                DeviceAlarm deviceAlarm = SolutionUtil.deviceAlarmConcurrentHashMap.get(id);
                deviceAlarm.setOperationType(MessageStatus.getValue(103).getStatus());
                SolutionUtil.deviceAlarmConcurrentHashMap.put(id,deviceAlarm);
                SolutionUtil.sendMessageConcurrentHashMap.remove(id);
            }else{
                SolutionUtil.sendMessageConcurrentHashMap.put(id,count+1);
                producerMessage.retrySendMessage(id);
            }
        }
    }
}
