package com.david.message.solution.producer;

import com.alibaba.fastjson.JSON;
import com.david.message.solution.callBack.SendMessageCallBack;
import com.david.message.solution.common.RabbitServer;
import com.david.message.solution.common.SolutionUtil;
import com.david.message.solution.common.exchange.RabbitMQExchange;
import com.david.message.solution.domian.DeviceAlarm;
import com.david.message.solution.domian.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

//@Component
public class SendMessage {
    public final static long SECOND = 1 * 1000;

    @Autowired
    private RabbitMQExchange topicRabbitMQ;
    @Autowired
    private RabbitServer rabbitServer;
    @Autowired
    private SendMessageCallBack sendMessageCallBack;

   // @Scheduled(fixedRate = SECOND * 60 * 1, initialDelay = SECOND * 10)
    public void syncMessage (){
        DeviceAlarm deviceAlarm = handleMessage();
        if(deviceAlarm.getOperType()==-1){
            rabbitServer.setRabbitExchangeQueue(topicRabbitMQ);
            rabbitServer.setRabbitCallback(sendMessageCallBack);
            SolutionUtil.deviceAlarmConcurrentHashMap.put(deviceAlarm.getId(),deviceAlarm);
            String message = JSON.toJSONString(deviceAlarm);
            rabbitServer.sendMessageAndCallback(message,"solution_topic","solution_message", deviceAlarm.getId());
        }
    }
    private DeviceAlarm handleMessage (){
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setName("路灯照明设备");
        deviceInfo.setStatus("告警");
        deviceInfo.setDegrees(0);
        String deviceInfoMessage = JSON.toJSONString(deviceInfo);
        DeviceAlarm deviceAlarm = new DeviceAlarm();
        deviceAlarm.setId("QWE123ASD");
        deviceAlarm.setMessage(deviceInfoMessage);
        deviceAlarm.setOperType(-1);
        deviceAlarm.setCreateTime(new Date());
        deviceAlarm.setUpdateTime(new Date());
        return deviceAlarm;
    }
}
