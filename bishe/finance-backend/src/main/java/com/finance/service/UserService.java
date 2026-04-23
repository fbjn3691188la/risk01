package com.finance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.finance.entity.User;
import com.finance.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
               .eq("password", password);
        return userMapper.selectOne(wrapper);
    }

    public User findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }
}