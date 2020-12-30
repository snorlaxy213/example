### 为什么使用Redis？

1. redis数据库中的所有数据都存储在内存中，，内存的读写速度远远大于硬盘的速度，因此查询速度对于其他基于硬盘的数据库又明显优势；
2. 在高并发的环境下，大量请求直接访问数据库，会导致数据库出现异常（单个Mysql数据库大约支持500-1000的并发），这种情况使用Redis做缓冲操作（保存热点数据），可以缓解数据库压力；

### Redis优点：

1. 运行在内存，读写速度快；
2. 支持数据持久化，即可以将内存中的数据异步写入到硬盘中，同时不影响继续提供服务；
3. 支持数据结构丰富；

### 数据类型：

1. string（字符串）： 一个 key 对应一个 value； 可以包含任何数据，比如jpg图片或者序列化的对象；string 类型的值最大能存储 512MB。

   ````shell
   redis 127.0.0.1:6379> SET runoob "菜鸟教程"
   OK
   redis 127.0.0.1:6379> GET runoob
   "菜鸟教程"
   ````

2. list（链表）： 简单的字符串列表，按照插入顺序排序

   ```shell
   redis 127.0.0.1:6379> lpush runoob redis
   (integer) 1
   redis 127.0.0.1:6379> lpush runoob mongodb
   (integer) 2
   redis 127.0.0.1:6379> lpush runoob rabitmq
   (integer) 3
   redis 127.0.0.1:6379> lrange runoob 0 10
   1) "rabitmq"
   2) "mongodb"
   3) "redis"
   ```

3. set（集合） Redis 的 Set 是 string 类型的无序集合。(不可重复)

   集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。 sadd 命令:添加一个 string 元素到 key 对应的 set 集合中，成功返回 1，如果元素已经在集合中返回 0。

   ```shell
   redis 127.0.0.1:6379> sadd runoob redis
   (integer) 1
   redis 127.0.0.1:6379> sadd runoob mongodb
   (integer) 1
   redis 127.0.0.1:6379> sadd runoob rabitmq
   (integer) 1
   redis 127.0.0.1:6379> sadd runoob rabitmq
   (integer) 0
   redis 127.0.0.1:6379> smembers runoob
   
   1) "redis"
   2) "rabitmq"
   3) "mongodb"
   ```

4. zset（sorted set - 有序集合） Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。

   不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

   zset的成员是唯一的,但分数(score)却可以重复。 zadd 命令: 添加元素到集合，元素在集合中存在则更新对应score

   ```shell
   redis 127.0.0.1:6379> zadd runoob 0 redis
   (integer) 1
   redis 127.0.0.1:6379> zadd runoob 0 mongodb
   (integer) 1
   redis 127.0.0.1:6379> zadd runoob 0 rabitmq
   (integer) 1
   redis 127.0.0.1:6379> zadd runoob 0 rabitmq
   (integer) 0
   redis 127.0.0.1:6379> > ZRANGEBYSCORE runoob 0 1000
   1) "mongodb"
   2) "rabitmq"
   3) "redis"
   ```

5. Hash（哈希）：hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。

   ```shell
   redis 127.0.0.1:6379> HMSET runoob field1 "Hello" field2 "World"
   "OK"
   redis 127.0.0.1:6379> HGET runoob field1
   "Hello"
   redis 127.0.0.1:6379> HGET runoob field2
   "World"
   ```

### Redis事务

[Redis事务](https://zhuanlan.zhihu.com/p/76957867)

### Redis最适用场景

1. 会话缓存session cache
2. 排行榜/计数器
3. 发布/订阅

### **缓存淘汰策略**

（1）：先进先出算法（FIFO）

（2）：最近使用最少Least Frequently Used（LFU）

（3）：最长时间未被使用的Least Recently Used（LRU）

当存在热点数据时，LRU的效率很好，但偶发性的、周期性的批量操作会导致LRU命中率急剧下降，缓存污染情况比较严重

### **缓存雪崩以及处理办法**

同一时刻大量缓存失效；

#### 处理方法：

（1）：缓存数据增加过期标记

（2）：设置不同的缓存失效时间

（3）：双层缓存策略C1为短期，C2为长期

（4）：定时更新策略

### **缓存击穿原因以及处理办法**

频繁请求查询系统中不存在的数据导致；

#### 处理方法：

（1）：cache null策略，查询反馈结果为null仍然缓存这个null结果，设置不超过5分钟过期时间

