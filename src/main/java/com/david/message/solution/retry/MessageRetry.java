package com.david.message.solution.retry;

import com.david.message.solution.common.ProducerMessage;
import com.rabbitmq.client.Channel;

public interface MessageRetry {

    /**
     * 重试次数超出后加入死信队列 [注意该队列配置了死信队列]
     * @param message           重试消息
     * @param message    消息
     * @param retryCount        消息重试的次数
     */
    void retryConsumeAfterDeadQueue(Channel channel, String message,long deliveryTag, int retryCount);


    /**
     * 重试次数超出后做其他业务处理,然后采用ACK 丢弃这条消息
     * @param message           重试消息
     * @param message    消息唯一标识
     * @param retryCount        消息重试的次数
     */
   void retryConsumeAfterOtherThing(Channel channel, String message,long deliveryTag,int retryCount);

    /**
     * 重试发送消息
     * @param id            发送消息唯一标识
     * @param retryCount    重试次数
     */
   void retryPushAfterOtherThing(ProducerMessage producerMessage, String id, int retryCount);
}
