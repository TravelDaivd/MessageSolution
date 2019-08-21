package com.david.message.solution.exchange;

import org.springframework.amqp.core.*;

/**
 * 交换器和队列默认都是持久化
 */

public class DirectRabbit  implements RabbitMQExchange {

    private String exchangeName;
    private String queueName;
    private String routingKey;
    private boolean setDeadQueue;

    public DirectRabbit(){}

    public DirectRabbit(String exchangeName, String queueName, String routingKey,boolean setDeadQueue) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.setDeadQueue = setDeadQueue;
    }

    @Override
    public void createExchange(AmqpAdmin amqpAdmin) {
        DirectExchange directExchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(directExchange);
    }

    @Override
    public void createQueue(AmqpAdmin amqpAdmin) {
        if(setDeadQueue){
            DirectRabbit directRabbit = new DirectRabbit();
            directRabbit.deleteNotDeadQueue(amqpAdmin,queueName);
            directRabbit.createDeadQueue(amqpAdmin,queueName,exchangeName,routingKey,0);
        }else{
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
        }
    }

    @Override
    public void bindingExchangeAndQueue(AmqpAdmin amqpAdmin) {
        createExchange(amqpAdmin);
        createQueue(amqpAdmin);
        if(setDeadQueue){
            DirectRabbit directRabbit = new DirectRabbit();
            directRabbit.bindingDeadQueue(amqpAdmin,exchangeName,queueName,routingKey);
        }
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey,null);
        amqpAdmin.declareBinding(binding);
    }

}
