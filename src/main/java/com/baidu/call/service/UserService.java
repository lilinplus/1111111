package com.baidu.call.service;

import com.baidu.call.pojo.UserAreaVo;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.SelectText;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface UserService {

    Msg addUser(UserAreaVo userAreaVo);

    Msg deleteUser(Long userId);

    Msg updateUser(Long userId,UserAreaVo userAreaVo);

    Pager queryUser(Pager pager);

    //域账户查询用户信息
    Msg getUserInfoOne(String userName);

    SelectText findUserByUsernameUic(String username);

}
