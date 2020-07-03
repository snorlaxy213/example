package com.vino.controller;

import java.util.List;

import com.vino.mapper.primary.UserPrimaryMapper;
import com.vino.mapper.slave.UserSlaveMapper;
import com.vino.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserPrimaryMapper userPrimaryMapper;

	@Autowired
	private UserSlaveMapper userSlaveMapper;
	
	@RequestMapping("/getUsers")
	public List<User> getUsers() {
        return userPrimaryMapper.getAll();
	}
	
    @RequestMapping("/getUser")
    public User getUser(Long id) {
        return userSlaveMapper.getOne(id);
    }
    
    @RequestMapping("/add")
    public void save(@RequestBody User user) {
        userSlaveMapper.insert(user);
    }
    
    @RequestMapping(value="update")
    public void update(@RequestBody User user) {
        userSlaveMapper.update(user);
    }
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userPrimaryMapper.delete(id);
    }
    
}