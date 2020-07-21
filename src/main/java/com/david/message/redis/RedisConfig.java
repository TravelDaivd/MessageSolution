package com.david.message.redis;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gulei
 */
@Configuration
public class RedisConfig {

    @Bean("redisAsyncCommands")
    public static RedisAsyncCommandsImpl getRedisAsyncCommands(){
        return new RedisAsyncCommandsImpl(RedisCommon.getStatefulConnection(), StringCodec.UTF8);
    }

    @Bean("RedisCommands")
    public static RedisCommands getRedisCommands(){
        return RedisCommon.getStatefulConnection().sync();
    }

}
