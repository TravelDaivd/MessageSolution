package com.david.message.solution.common.exchange;


import org.springframework.amqp.core.AmqpAdmin;

public interface RabbitMQExchange {

    void createExchange(AmqpAdmin amqpAdmin);

    void createQueue(AmqpAdmin amqpAdmin);

    void bindingExchangeAndQueue(AmqpAdmin amqpAdmin);
}
