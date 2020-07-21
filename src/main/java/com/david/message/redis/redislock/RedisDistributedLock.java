package com.david.message.redis.redislock;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用redis分布式锁解决缓存雪崩
 * @author gulei
 */
@Component
public class RedisDistributedLock implements AsyncLockInterface {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisAsyncCommandsImpl<String,String> redisAsyncCommands;


    @Override
    public  boolean lockUpEX(String lockKey, String lockValue,long expireTime) throws RedisException {
        if(existLock(lockKey)){
           return false;
        }
        SetArgs setArgs = new SetArgs();
        setArgs.nx();     //仅在键不存在时设置键。
        //设置指定的到期时间(以秒为单位)
        setArgs.ex(expireTime);
        RedisFuture<String> redisFuture = redisAsyncCommands.set(lockKey, lockValue, setArgs);
        try{
            return redisFuture.get().equals("OK");
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("加锁失败：key：{0};value：{1};错误信息：{2}",lockKey,lockValue,e.getMessage())));
            throw new RedisException(e.getMessage(),redisFuture);
        }
    }


    @Override
    public boolean unLock(String key, String value) {
        try{
            Long result = this.unOrExistLock(key,value,true).get();
            return result != null && result > 0;
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("解锁失败：key：{0};value：{1};错误信息：{2}",key,value,e.getMessage())));
        }
        return false;
    }




    @Override
    public  boolean existLock(String key,String value) {
        try{
            Long result = this.unOrExistLock(key,value,false).get();
            return  result != null && result > 0;
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("查看是否存在失败：key：{0};错误信息：{1}",key,e.getMessage())));
            return false;
        }
    }

    @Override
    public synchronized boolean existLock(String key) {
        RedisFuture<Long> redisFuture = redisAsyncCommands.exists(key);
        try{
            Long result = redisFuture.get();
            return  result != null && result > 0;
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("查看是否存在失败：key：{0};错误信息：{1}",key,e.getMessage())));
            return false;
        }
    }

    @Override
    public String getLockValue(String key) {

        RedisFuture<String> redisFuture = redisAsyncCommands.get(key);
        try{
            String value = redisFuture.get();
            return  value;
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("获取value失败：key：{0};错误信息：{1}",key,e.getMessage())));
            return null;
        }
    }

    @Override
    public String getLuaValue(String key) {
        RedisFuture<String> redisFuture = redisAsyncCommands.eval(AsyncLockInterface.setLuaScript(),
                ScriptOutputType.VALUE, key);
        try{
            return  redisFuture.get();
        }catch (Exception e){
            e.printStackTrace();
            logger.error((MessageFormat.format("获取value失败：key：{0};错误信息：{1}",key,e.getMessage())));
            return null;
        }
    }

    private  RedisFuture<Long> unOrExistLock(String key,String value,boolean bool){
        List<String> args = new ArrayList<>();
        args.add(value);
        String[] keyArray = {key};
        RedisFuture<Long> redisFuture = redisAsyncCommands.eval(AsyncLockInterface.setLuaScript(bool),
                ScriptOutputType.INTEGER, keyArray, value);
        return redisFuture;
    }
}
