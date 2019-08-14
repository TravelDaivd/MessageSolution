package com.david.message.solution.common;

import com.david.message.solution.domian.DeviceAlarm;

import java.util.concurrent.ConcurrentHashMap;

public class SolutionUtil {
    public static ConcurrentHashMap<String, DeviceAlarm> deviceAlarmConcurrentHashMap = new ConcurrentHashMap<>();

}
