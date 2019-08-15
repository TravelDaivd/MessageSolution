package com.david.message.solution.common.exchange;

import org.springframework.amqp.core.*;

public class TopicRabbit implements RabbitMQExchange {
    private String exchangeName;
    private String queueName;
    private String routingKey;
    private boolean setDeadQueue;

    public TopicRabbit(){}

    public TopicRabbit(String exchangeName, String queueName, String routingKey,boolean setDeadQueue) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.setDeadQueue = setDeadQueue;
    }

    @Override
    public void createExchange(AmqpAdmin amqpAdmin) {
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(topicExchange);
    }

    @Override
    public void createQueue(AmqpAdmin amqpAdmin) {
        if(setDeadQueue){
            TopicRabbit topicRabbit = new TopicRabbit();
            topicRabbit.createDeadQueue(amqpAdmin,queueName,exchangeName,routingKey,0);
        }else{
            Queue queue  = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
        }

    }

    @Override
    public void bindingExchangeAndQueue(AmqpAdmin amqpAdmin) {
        createExchange(amqpAdmin);
        createQueue(amqpAdmin);
        if(setDeadQueue){
            TopicRabbit topicRabbit = new TopicRabbit();
            topicRabbit.bindingDeadQueue(amqpAdmin,exchangeName,queueName,routingKey);
        }
        Binding binding = BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }
}
