
package net.guides.springboot.springbootmultipledatasources.services;

import net.guides.springboot.springbootmultipledatasources.Thread.PrescriptionsThread;
import net.guides.springboot.springbootmultipledatasources.orders.entities.Order;
import net.guides.springboot.springbootmultipledatasources.orders.repositories.OrderRepository;
import net.guides.springboot.springbootmultipledatasources.security.entities.User;
import net.guides.springboot.springbootmultipledatasources.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * @author Ramesh Fadatare
 *
 */
@Service
public class UserOrdersService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	Executor asyncServiceExecutor;
	
	@Transactional(transactionManager="securityTransactionManager")
	public List<User> getUsers()
	{
		return userRepository.findAll();
	}
	
	@Transactional(transactionManager="ordersTransactionManager")
	public List<Order> getOrders()
	{
		return orderRepository.findAll();
	}

	// --------------------------- new update ----------------------------
	public void saveUser(List<User> list) throws Exception {

		//一个线程处理500条数据
		int count = 500;
		//数据集合大小
		int listSize = list.size();
		//开启的线程数
		int runSize = (listSize / count) + 1;
		//存放每个线程的执行数据
		List<User> sublist = null;
		Integer mun = 0;
		CountDownLatch begin = new CountDownLatch(1);
		CountDownLatch end = new CountDownLatch(runSize);
		//循环创建线程
		for (int i = 0; i < runSize; i++) {
			//计算每个线程执行的数据
			int startIndex = (i * count);
			int endIndex;
			if ((i + 1) == runSize) {
				endIndex = list.size();
			} else {
				endIndex = (i + 1) * count;
			}
			sublist = list.subList(startIndex, endIndex);
			//线程类
			PrescriptionsThread prescriptionsThread = new PrescriptionsThread(sublist, begin, end, userRepository);
			asyncServiceExecutor.execute(prescriptionsThread);
			mun = prescriptionsThread.getCount();
		}
		while (count == mun){
			break;
		}
		begin.countDown();
		end.await();
	}
}
