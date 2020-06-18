package com.vino.web;

import java.util.List;

import com.vino.mapper.primary.UserPrimaryMapper;
import com.vino.mapper.slave.UserSlaveMapper;
import com.vino.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
		List<User> users=userPrimaryMapper.getAll();
		return users;
	}
	
    @RequestMapping("/getUser")
    public User getUser(Long id) {
    	User user=userSlaveMapper.getOne(id);
        return user;
    }
    
    @RequestMapping("/add")
    public void save(User user) {
        userSlaveMapper.insert(user);
    }
    
    @RequestMapping(value="update")
    public void update(User user) {
        userSlaveMapper.update(user);
    }
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userPrimaryMapper.delete(id);
    }
    
}