package com.vino.controller;

import java.util.UUID;

import com.vino.util.SnowflakeIdWorker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1. 分表分库ID怎么处理？
 * 2. 如何设计动态扩容缩容的分表分库方案？（如何处理ID扩容的问题）
 */
@RestController
public class DistributeController {

    /**
     *
     *
     * */
    //https://colobu.com/2020/02/21/ID-generator/

    /**
     * 1.UUID/GUID
     *
     * 优点:
     * 容易实现，产生快
     * ID唯一(几乎不会产生重复id)
     * 无需中心化的服务器(本地生成，不会基于数据库)
     * 不会泄漏商业机密
     *
     * 缺点:
     * 可读性差
     * UUID太长(16个字节)，占用空间太多 ，作为主键性能很差；
     * 影响数据库的性能, 比如UUID or GUID as Primary Keys? Be Careful!
     * --UUID不具有有序性，会导致 B+ 树索引在写的时候有过多的随机写操作（连续的 ID 可以产生部分顺序写）；
     *
     * 适用场景：文件名、编号。。。。
     */
    @GetMapping(value = "uuid")
    public String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 2.递增的整数:
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
     * 使用方法：
     * Mysql：设置每个数据库递增的起始值和步长（步长为服务数据库的个数）
     * Oracle： 设置每个Sequence的起始值和步长（步长为服务数据库的个数）
     * 适用场景：在用户防止产生的 ID 重复时，这种方案实现起来比较简单，也能达到性能目标。但是服务节点固定，步长也固定，将来如果还要增加服务节点，就不好搞了。
     *
     */

    /**
     * 3.snowflake 算法是 twitter 开源的分布式 id 生成算法，采用 Scala 语言实现，是把一个 64 位的 long 型的 id，
     * 1 个 bit 是不用的，用其中的 41 bits 作为毫秒数，用 10 bits 作为工作机器 id，12 bits 作为序列号。
     *
     * 优点：
     * 存储少, 8个字节
     * 可读性高
     * 性能好，可以中心化的产生ID,也可以独立节点生成
     *
     * 缺点：
     * 时间回拨会重复产生ID
     * ID生成有规律性,信息容易泄漏
     *
     */
    @GetMapping(value = "snowFlakeId")
    public Long snowFlakeId() {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        return idWorker.nextId();
    }
}
