### 线程池配置参数说明

| 配置属性         | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| pool-size        | core size：最小的线程数，缺省：1<br/>max size：最大的线程数，缺省：Integer.MAX_VALUE |
| queue-capacity   | 当线程池中最小的线程数被占用满后，新的任务会被放进队列queue里面，当这个queue的capacity也被占满之后，pool里面会创建新线程处理这个任务，直到总线程数达到了最大线程数maxsize，这时系统会拒绝这个任务并抛出TaskRejectedException异常（缺省配置的情况下，可以通过rejection-policy来决定如何处理这种情况）。缺省值为：Integer.MAX_VALUE |
| keep-alive       | 超过coresize最小线程数的那些线程，任务完成后，再经过这个设置的时长（单位:秒）会被结束掉,这样的话线程池可以动态的调整池中的线程数 |
| rejection-policy | ABORT（缺省）：抛出TaskRejectedException异常，然后不执行<br/>DISCARD：不执行，也不抛出异常即放弃该线程<br/>DISCARD_OLDEST：丢弃queue中最旧的那个任务<br/>CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行(不再异步) |



### BUG：异步运行时，应用关闭而异步任务仍执行导致的错误

解决方案：

1. 线程池参数设置：`setWaitForTasksToCompleteOnShutdown(true)`用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。
2. `setAwaitTerminationSeconds(60)`*设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住*

