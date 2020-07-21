package com.david.message.config;

import com.david.message.solution.exchange.DirectRabbit;
import com.david.message.solution.exchange.FanoutRabbit;
import com.david.message.solution.exchange.RabbitMQExchange;
import com.david.message.solution.exchange.TopicRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {


    @Bean("topicRabbitMQ")
    public RabbitMQExchange topicExchange(){
        return new TopicRabbit("solution_topic","solutionTopic","solution_message",true);
    }

    @Bean("directRabbitMQ")
    public RabbitMQExchange directExchange(){
        return new DirectRabbit("solution_direct","solutionDirect","solution_message",false);
    }

    @Bean("fanoutRabbitMQ")
    public RabbitMQExchange fanoutExchange(){
        return new FanoutRabbit("solution_fanout","solutionFanout");
    }



}
