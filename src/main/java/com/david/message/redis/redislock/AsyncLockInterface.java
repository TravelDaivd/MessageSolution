package com.david.message.redis.redislock;

/*异步加锁接口*/
public interface AsyncLockInterface {

    /**
     *
     * @param isDelete true:删除 ；false检查是否存在
     * @return
     */
    static String setLuaScript(boolean isDelete){
        StringBuilder luaData = new StringBuilder();
        luaData.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        luaData.append("then ");
        if(isDelete){
            luaData.append(" return redis.call(\"del\",KEYS[1]) ");
        }else{
            luaData.append(" return 1 ");
        }
        luaData.append("else ");
        luaData.append("    return 0 ");
        luaData.append("end ");
        return  luaData.toString();
    }

    static String setLuaScript(){
        StringBuilder luaData = new StringBuilder();
        luaData.append(" return redis.call(\"get\",KEYS[1]) ");
        return  luaData.toString();
    }



    /**
     * 加锁 到期时间(以秒为单位)
     * @param key
     * @param obj
     * @return
     */
    boolean lockUpEX(String key, String obj, long expireTime) throws RedisException;

    /**
     * 解锁
     * @param key
     * @param obj
     * @return
     */
    boolean unLock(String key, String obj);

    /**
     * 锁是否存在
     * @param key
     * @return
     */
    boolean existLock(String key);
    boolean existLock(String key, String value);

    String getLockValue(String key);

    String getLuaValue(String key);
}
