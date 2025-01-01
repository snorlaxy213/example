package milliondata.service;

import milliondata.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @className UserService
 * @description 用户管理
 * @author Chen Jiaying
 * @date 2021/10/19
 **/
public interface UserService {

    /**
     * 添加用户
     * @param user 用户数据
     * @return 是否成功
     */
    Integer addUser(User user);

    /**
     * 多线程插入用户
     * @param users 用户数据
     * @return 插入成功/失败信息
     * @exception ExecutionException ...
     * @exception InterruptedException ...
     */
    Map<String, Object> importData(List<User> users) throws ExecutionException, InterruptedException;
    
}
