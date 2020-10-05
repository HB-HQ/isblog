package com.hb.isblog.service;

import com.hb.isblog.entity.User;

public interface UserService {
    //    核对用户名和密码
    User checkUser(String username, String password);
}
