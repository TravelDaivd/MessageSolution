package com.david.message.redis;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.time.Duration;

/**
 * @author gulei
 */
public class RedisCommon {

    public static RedisClient getRedisClient(){
        RedisURI redisURI = new RedisURI();
        redisURI.setHost("127.0.0.1");
        redisURI.setPort(6379);
        redisURI.setTimeout(Duration.ofSeconds(100));
        return RedisClient.create(redisURI);
    }

    public  static StatefulRedisConnection getStatefulConnection(){
        return getRedisClient().connect();
    }





}
