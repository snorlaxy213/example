package com.vino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DistributeController {

    //https://colobu.com/2020/02/21/ID-generator/

    /**
     * UUID/GUID
     *
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

    /*
     * 递增的整数:
     * 可以通过关系型数据库的自增主键产生唯一的ID，现在流行的商业数据库都支持自增主键的特性，比如mysql等。
     * nosql数据库也提供类似特性，比如Redis。
     *
     * 优点:
     * 容易产生
     * 可读性好，容易记住
     * 存储很小，比如4个字节
     *
     * 缺点:
     * 需要中心化的服务器，并且需要处理单点的问题，而且单点有性能瓶颈的问题
     * 如果ID暴露给公共访问，可能会泄漏商业机密
     * 需要访问一次数据库获取ID
     *
     */



}
