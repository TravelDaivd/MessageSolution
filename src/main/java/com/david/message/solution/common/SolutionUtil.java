package com.david.message.solution.common;

import com.david.message.solution.domian.DeviceAlarm;

import java.util.concurrent.ConcurrentHashMap;

public class SolutionUtil {
    public static final String DEAD_QUEUE_NAME = "redirect_";

    public static ConcurrentHashMap<String, DeviceAlarm> deviceAlarmConcurrentHashMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,Integer> receiveMessageConcurrentHashMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String,Integer> sendMessageConcurrentHashMap = new ConcurrentHashMap<>();

    public static String toHash(String key) {
        int arraySize = 11113;
        // 数组大小一般取质数
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) {
            // 从字符串的左边开始计算
            int letterValue = key.charAt(i) - 96;
            // 将获取到的字符串转换成数字，比如a的码值是97，则97-96=1
            // 就代表a的值，同理b=2；
            hashCode = ((hashCode << 5) + letterValue) % arraySize;
            // 防止编码溢出，对每步结果都进行取模运算
        }
        return String.valueOf(hashCode);
    }

}
