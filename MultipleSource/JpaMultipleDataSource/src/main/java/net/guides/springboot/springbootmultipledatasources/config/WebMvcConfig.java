package net.guides.springboot.springbootmultipledatasources.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Ramesh Fadatare
 * 
 */
@Configuration
@EnableAsync
public class WebMvcConfig {

	@Value("${async.executor.thread.core_pool_size}")
	private int corePoolSize;
	@Value("${async.executor.thread.max_pool_size}")
	private int maxPoolSize;
	@Value("${async.executor.thread.queue_capacity}")
	private int queueCapacity;
	@Value("${async.executor.thread.name.prefix}")
	private String namePrefix;

	@Bean
    public OpenEntityManagerInViewFilter securityOpenEntityManagerInViewFilter()
    {
    	OpenEntityManagerInViewFilter osivFilter = new OpenEntityManagerInViewFilter();
    	osivFilter.setEntityManagerFactoryBeanName("securityEntityManagerFactory");
    	return osivFilter;
    }
	
	@Bean
    public OpenEntityManagerInViewFilter ordersOpenEntityManagerInViewFilter()
    {
    	OpenEntityManagerInViewFilter osivFilter = new OpenEntityManagerInViewFilter();
    	osivFilter.setEntityManagerFactoryBeanName("ordersEntityManagerFactory");
    	return osivFilter;
    }

	/**
	 * 配置核心线程池
	 */
	@Bean
	public Executor asyncServiceExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程大小
		executor.setCorePoolSize(corePoolSize);
		// 最大县城大小
		executor.setMaxPoolSize(maxPoolSize);
		// 当线程不工作时最大存活时间
		executor.setKeepAliveSeconds(30);
		// 打到最大线程且阻塞队列已满时，所设定的拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		//执行初始化
		executor.initialize();
		return executor.getThreadPoolExecutor();
	}
}