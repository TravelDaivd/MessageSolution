package com.david.message.solution.exchange;


import com.david.message.solution.common.SolutionUtil;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public interface RabbitMQExchange {


    void createExchange(AmqpAdmin amqpAdmin);

    void createQueue(AmqpAdmin amqpAdmin);

    void bindingExchangeAndQueue(AmqpAdmin amqpAdmin);

    /**
     * 创建死信队列
     * @param amqpAdmin
     * @param queueName  队列名称
     * @param exchangeName  交换器名称
     * @param redirectRoutingKey    替补队列路由键
     * @param expirationTime   设置消息过期时间(秒)，将会针对整个队列中消息
     * @return
     */
    default void createDeadQueue(AmqpAdmin amqpAdmin,String queueName,String exchangeName,
                                               String redirectRoutingKey,int expirationTime){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("x-dead-letter-exchange",exchangeName);
        paramMap.put("x-dead-letter-routing-key", SolutionUtil.DEAD_QUEUE_NAME +redirectRoutingKey);
        if(expirationTime > 0){
            paramMap.put("x-message-ttl",expirationTime*1000);
        }
        Queue redirectQueue = new Queue(SolutionUtil.DEAD_QUEUE_NAME+queueName);
        Queue queue = new Queue(queueName,true,false,false,paramMap);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareQueue(redirectQueue);
    }

    /**
     * 绑定死信队列
     * @param amqpAdmin
     * @param exchangeName  交换器名称
     * @param redirectQueueName    替补队列路由键
     * @param redirectRoutingKey     替补队列名称
     */
    default void bindingDeadQueue(AmqpAdmin amqpAdmin,String exchangeName,String redirectQueueName,String redirectRoutingKey){
        Binding binding = BindingBuilder.bind(new Queue(SolutionUtil.DEAD_QUEUE_NAME+redirectQueueName)).
                to(new TopicExchange(exchangeName)).with(SolutionUtil.DEAD_QUEUE_NAME+redirectRoutingKey);
        amqpAdmin.declareBinding(binding);
    }

    /**
     * 重新定义队列类型
     * @param amqpAdmin
     * @param queueName
     */
    default void deleteNotDeadQueue(AmqpAdmin amqpAdmin,String queueName){
        Properties deadQueue = amqpAdmin.getQueueProperties(queueName);
        Properties redirectQueue = amqpAdmin.getQueueProperties(SolutionUtil.DEAD_QUEUE_NAME+queueName);
        if(deadQueue == null && redirectQueue == null){}else{
            if(redirectQueue == null && deadQueue.size() > 0){
                amqpAdmin.deleteQueue(queueName);
            }
        }

    }

}
