package com.david.message.solution.producer;

import com.david.message.rabbit.callback.SendMessageCallBack;
import com.david.message.rabbit.common.MessageStatus;
import com.david.message.rabbit.common.ProducerMessage;
import com.david.message.rabbit.common.RabbitServer;
import com.david.message.util.SolutionUtil;
import com.david.message.solution.exchange.RabbitMQExchange;
import com.david.message.solution.item.module.DeviceAlarm;
import com.david.message.solution.item.module.DeviceInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SendMessage implements ProducerMessage<DeviceAlarm> {
    public final static long SECOND = 1 * 1000;

    @Autowired
    private RabbitMQExchange topicRabbitMQ;
    @Autowired
    private RabbitServer rabbitServer;
    @Autowired
    private SendMessageCallBack sendMessageCallBack;

   // @Scheduled(fixedRate = SECOND * 60 * 1, initialDelay = SECOND * 5)
    public void syncMessage (){
        DeviceAlarm deviceAlarm = handleMessage();
        sendMessage(deviceAlarm);
    }

    @Override
    public void sendMessage(DeviceAlarm deviceAlarm) {
        if(deviceAlarm.getOperationType().equals(MessageStatus.getValue(101).getStatus())){
            rabbitServer.setRabbitExchangeQueue(topicRabbitMQ);
            rabbitServer.setRabbitCallback(sendMessageCallBack);
            SolutionUtil.deviceAlarmConcurrentHashMap.put(deviceAlarm.getId(),deviceAlarm);
            Gson gson = new Gson();
            String message = gson.toJson(deviceAlarm);
            rabbitServer.sendMessageAndCallback(message,"solution_topic","solution_message", deviceAlarm.getId());
        }
    }

    @Override
    public void retrySendMessage(String id) {
        DeviceAlarm deviceAlarm = SolutionUtil.deviceAlarmConcurrentHashMap.get(id);
        sendMessage(deviceAlarm);
    }

    private DeviceAlarm handleMessage (){
        String [] array= {"QWE123ASD","QW736FGD","GG536D"};
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setName("路灯照明设备");
        deviceInfo.setStatus("告警");
        deviceInfo.setDegrees(0);
        Gson gson = new Gson();
        String deviceInfoMessage = gson.toJson(deviceInfo);
        DeviceAlarm deviceAlarm = new DeviceAlarm();
        deviceAlarm.setId(array[2]);
        deviceAlarm.setMessage(deviceInfoMessage);
        deviceAlarm.setOperationType(MessageStatus.getValue(101).getStatus());
        deviceAlarm.setCreateTime(new Date());
        deviceAlarm.setUpdateTime(new Date());
        return deviceAlarm;
    }
}
