package com.cjh.websocket.controller;

import com.cjh.websocket.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/publish")
    public void publish(@RequestParam("str") String str) {
        redisUtil.sendMessage("PRESCRIPTION", str);
    }
}
