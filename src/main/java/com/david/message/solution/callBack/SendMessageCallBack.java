package com.david.message.solution.callBack;

import com.david.message.solution.common.RabbitCallback;
import com.david.message.solution.common.SolutionUtil;
import com.david.message.solution.domian.DeviceAlarm;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public class SendMessageCallBack implements RabbitCallback {

    @Override
    public void messageSendSuccess(String id) {
        DeviceAlarm deviceAlarm = SolutionUtil.deviceAlarmConcurrentHashMap.get(id);
        deviceAlarm.setOperType(0);
        SolutionUtil.deviceAlarmConcurrentHashMap.put(id,deviceAlarm);
    }

    @Override
    public void messageSendFail(String id) {
        
    }

    @Override
    public void messageSendFailReturn(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }
}
