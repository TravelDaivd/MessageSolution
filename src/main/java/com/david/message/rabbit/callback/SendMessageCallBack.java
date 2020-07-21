package com.david.message.rabbit.callback;

import com.david.message.rabbit.common.*;
import com.david.message.solution.item.module.DeviceAlarm;
import com.david.message.rabbit.retry.AbstractMessageRetry;
import com.david.message.rabbit.retry.MessageRetry;
import com.david.message.util.SolutionUtil;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gulei
 */
@Component
public class SendMessageCallBack implements RabbitCallback {
    @Autowired
    private ProducerMessage sendMessage;

    @Override
    public void messageSendSuccess(String id) {
        DeviceAlarm deviceAlarm = SolutionUtil.deviceAlarmConcurrentHashMap.get(id);
        deviceAlarm.setOperationType(MessageStatus.getValue(100).getStatus());
        SolutionUtil.deviceAlarmConcurrentHashMap.put(id,deviceAlarm);
    }

    @Override
    public void messageSendFail(String id) {
        MessageRetry abstractMessageRetry = new AbstractMessageRetry();
        abstractMessageRetry.retryPushAfterOtherThing(sendMessage,id,10);
    }

    @Override
    public void messageSendFailReturn(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }
}
