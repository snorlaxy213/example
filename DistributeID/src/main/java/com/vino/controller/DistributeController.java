package com.vino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DistributeController {

    /**
     * 优点:
     * 容易实现，产生快
     * ID唯一(几乎不会产生重复id)
     * 无需中心化的服务器
     * 不会泄漏商业机密
     *
     * 缺点:
     * 可读性差
     * 占用空间太多(16个字节)
     * 影响数据库的性能, 比如UUID or GUID as Primary Keys? Be Careful!
     *
     */
    @GetMapping(name = "uuid")
    public String UUID() {
        return UUID.randomUUID().toString();
    }


}
