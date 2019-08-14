package com.david.message.solution.common.exchange;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

/**
 * 交换器和队列默认都是持久化
 */

public class DirectRabbit  implements RabbitMQExchange {

    private String exchangeName;
    private String queueName;
    private String routingKey;


    public DirectRabbit(String exchangeName, String queueName, String routingKey) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    @Override
    public void createExchange(AmqpAdmin amqpAdmin) {
        DirectExchange directExchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(directExchange);
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
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey,null);
        amqpAdmin.declareBinding(binding);
    }

}
