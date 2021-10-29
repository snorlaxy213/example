package service;

import model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @className: UserService
 * @description: 用户管理
 * @author: Chen Jiaying
 * @date: 2021/10/19
 **/
public interface UserService {

    /**
     * 多线程插入用户
     * @param users 用户数据
     *
     */
    public Map<String, Object> importData(List<User> users) throws ExecutionException, InterruptedException;
}
