package com.david.message.solution.config;

import com.david.message.solution.common.exchange.DirectRabbit;
import com.david.message.solution.common.exchange.FanoutRabbit;
import com.david.message.solution.common.exchange.RabbitMQExchange;
import com.david.message.solution.common.exchange.TopicRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitPushConfig {


    @Bean("topicRabbitMQ")
    public RabbitMQExchange topicExchange(){
        return new TopicRabbit("solution_topic","solutionTopic","solution_message");
    }

    @Bean("directRabbitMQ")
    public RabbitMQExchange directExchange(){
        return new DirectRabbit("solution_direct","solutionDirect","solution_message");
    }

    @Bean("fanoutRabbitMQ")
    public RabbitMQExchange fanoutExchange(){
        return new FanoutRabbit("solution_fanout","solutionFanout");
    }
}
