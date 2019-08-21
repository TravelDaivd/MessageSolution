package com.david.message.solution.exchange;

import org.springframework.amqp.core.*;


public class FanoutRabbit  implements RabbitMQExchange {
    private String exchangeName;
    private String queueName;

    public FanoutRabbit(String exchangeName, String queueName) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
    }

    @Override
    public void createExchange(AmqpAdmin amqpAdmin) {
        FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
        amqpAdmin.declareExchange(fanoutExchange);
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
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, "",null);
        amqpAdmin.declareBinding(binding);
    }
}
