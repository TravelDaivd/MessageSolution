package com.david.message.solution.item.web;

import com.david.message.solution.item.module.UserInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 异步处理数据
 * @author gulei
 */

@RestController
public class AsyncController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 1、异步能否无返回值
     * 2、返回值能否由外部接收
     * @param userInfo
     * @return
     */

    @PostMapping("/add/user")
    public String addUser(@RequestBody UserInfo userInfo){

        Gson gson = new Gson();
        logger.info(String.format("input data is %s", gson.toJson(userInfo)));
        ExecutorService exs = Executors.newFixedThreadPool(4);
        AtomicReference<String> message = new AtomicReference<>("");
        CompletableFuture.supplyAsync(()->{
           handleDataOne(userInfo);
           return "用户";
        },exs).thenAcceptBoth(CompletableFuture.supplyAsync(()->{
            handleDataTwo(userInfo);
            return "添加";
        },exs),(s1,s2)-> message.set(s1 + s2));
        return "用户添加成功";
    }


    public void handleDataOne(UserInfo userInfo){
        logger.info("用户数据推送到rabbitmq");
    }


    public void handleDataTwo(UserInfo userInfo){
        logger.info("用户数据保存到数据库中");
    }


}
