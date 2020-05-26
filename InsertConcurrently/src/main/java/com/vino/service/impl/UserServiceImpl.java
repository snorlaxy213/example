package com.vino.service.impl;

import java.util.UUID;

import com.vino.enums.UserSexEnum;
import com.vino.mapper.master.MasterMapper;
import com.vino.model.User;
import com.vino.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());

    @Autowired
    MasterMapper masterMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @Async("asyncServiceExecutor")
    public Long addUser() {
        logger.info("线程-" + Thread.currentThread().getId() + "在执行写入");
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setUserName(UUID.randomUUID().toString().substring(0, 10));
            user.setPassWord(UUID.randomUUID().toString().substring(0, 10));
            user.setUserSex(UserSexEnum.MAN);
            masterMapper.insert(user);
        }

        return 0L;
    }

}