package com.david.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gulei
 */
@ConfigurationProperties(prefix = "david.message.solution")
@Component
public class MessageProperties {

    private String deadQueueName = "redirect_";

    public String getDeadQueueName() {
        return deadQueueName;
    }

    public void setDeadQueueName(String deadQueueName) {
        this.deadQueueName = deadQueueName;
    }
}
