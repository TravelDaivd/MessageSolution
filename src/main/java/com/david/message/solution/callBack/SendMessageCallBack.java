package com.david.message.solution.callBack;

import com.david.message.solution.common.*;
import com.david.message.solution.domian.DeviceAlarm;
import com.david.message.solution.retry.MessageRetry;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMessageCallBack implements RabbitCallback {

    @Autowired
    private ProducerMessage sendMessage;

    @Autowired
    private MessageRetry abstractMessageRetry;

    @Override
    public void messageSendSuccess(String id) {
        DeviceAlarm deviceAlarm = SolutionUtil.deviceAlarmConcurrentHashMap.get(id);
        deviceAlarm.setOperationType(MessageStatus.getValue(100).getStatus());
        SolutionUtil.deviceAlarmConcurrentHashMap.put(id,deviceAlarm);
    }

    @Override
    public void messageSendFail(String id) {
        abstractMessageRetry.retryPushAfterOtherThing(sendMessage,id,10);
    }

    @Override
    public void messageSendFailReturn(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }
}
