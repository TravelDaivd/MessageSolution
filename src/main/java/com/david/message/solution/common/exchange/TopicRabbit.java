package com.david.message.solution.common.exchange;

import org.springframework.amqp.core.*;

public class TopicRabbit implements RabbitMQExchange {
    private String exchangeName;
    private String queueName;
    private String routingKey;

    public TopicRabbit(String exchangeName, String queueName, String routingKey) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    @Override
    public void createExchange(AmqpAdmin amqpAdmin) {
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        amqpAdmin.declareExchange(topicExchange);
    }

    @Override
    public void createQueue(AmqpAdmin amqpAdmin) {
        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
    }

    @Override
    public void bindingExchangeAndQueue(AmqpAdmin amqpAdmin) {
        createExchange(amqpAdmin);
        createQueue(amqpAdmin);
        Binding binding = BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }
}
