## Quartz核心详解

Quartz框架中的重要参数:
1. Job和JobDetail 
2. JobExecutionContext 
3. JobDataMap 
4. Trigger、SimpleTrigger、CronTrigger

### Job和JobDetail

(1) Job是Quartz中的一个接口，接口下只有execute方法，在这个方法中编写业务逻辑。
(2)obDetail用来绑定Job，为Job实例提供许多属性：
- name
- group
- jobClass
- jobDataMap

JobDetail绑定指定的Job，每次Scheduler调度执行一个Job的时候，首先会拿到对应的Job，然后创建该Job实例，再去执行Job中的execute()的内容，任务执行结束后，关联的Job对象实例会被释放，且会被JVM GC清除。

**_为什么设计成JobDetail + Job，不直接使用Job?_**

`JobDetail定义的是任务数据，而真正的执行逻辑是在Job中。
这是因为任务是有可能并发执行，如果Scheduler直接使用Job，就会存在对同一个Job实例并发访问的问题。而JobDetail & Job 方式，Sheduler每次执行，都会根据JobDetail创建一个新的Job实例，这样就可以规避并发访问的问题。
`

### JobExecutionContext

JobExecutionContext中包含了Quartz运行时的环境以及Job本身的详细数据信息。
当Schedule调度执行一个Job的时候，就会将JobExecutionContext传递给该Job的execute()中，Job就可以通过JobExecutionContext对象获取信息。

### Trigger、SimpleTrigger、CronTrigger

- Trigger: Trigger是Quartz的触发器，会去通知Scheduler何时去执行对应Job。
- SimpleTrigger: SimpleTrigger可以实现在一个指定时间段内执行一次作业任务或一个时间段内多次执行作业任务。
- CronTrigger: CronTrigger功能非常强大，是基于日历的作业调度，而SimpleTrigger是精准指定间隔，所以相比SimpleTrigger，CroTrigger更加常用。