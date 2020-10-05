package com.hb.isblog.service.impl;

import com.hb.isblog.dao.UserDao;
import com.hb.isblog.entity.User;
import com.hb.isblog.service.UserService;
import com.hb.isblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @Description: 用户业务层接口实现类
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        return userDao.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}