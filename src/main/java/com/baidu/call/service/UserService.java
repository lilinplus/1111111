package com.baidu.call.service;

import com.baidu.call.model.User;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface UserService {
    Msg addUser(User user);
    Msg deleteUser(Long userId);
    Msg updateUser(Long userId,User user);
    Pager queryUser(Pager pager);
}
