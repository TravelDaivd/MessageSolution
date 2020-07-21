package com.david.message.rabbit.common;

/**
 * @author gulei
 */
public interface ProducerMessage<T> {

    /**
     * 发送消息
     */
    void sendMessage(T obj);

    /**
     * 发送失败后重新发送
     */
    void retrySendMessage(String id);

}
