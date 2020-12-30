package com.vino.web;

import com.vino.mapper.master.MasterMapper;
import com.vino.mapper.slave.SlaveMapper;
import com.vino.model.User;
import com.vino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MasterMapper masterMapper;

    @Autowired
    private SlaveMapper slaveMapper;

    @RequestMapping("/getUsers")
    public List<User> getUsers() {
        List<User> users = masterMapper.getAll();
        return users;
    }

    @RequestMapping("/getUser")
    public User getUser(Long id) {
        User user = slaveMapper.getOne(id);
        return user;
    }

    @RequestMapping("/add")
    public void save(User user) {
        userService.addUser();
        userService.addUser();
        userService.addUser();
        userService.addUser();
        userService.addUser();
        // slaveMapper.insert(user);
    }

    @RequestMapping(value = "update")
    public void update(User user) {
        slaveMapper.update(user);
    }

    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        masterMapper.delete(id);
    }

}