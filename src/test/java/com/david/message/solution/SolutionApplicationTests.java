package com.david.message.solution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolutionApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void contextLoads() {
        TopicExchange topicExchange = new TopicExchange("topic_rabbit_admin");
        amqpAdmin.declareExchange(topicExchange);
        Queue queue = new Queue("topic_admin_queue");
        amqpAdmin.declareQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("admin_queue");
        amqpAdmin.declareBinding(binding);

    }

}
