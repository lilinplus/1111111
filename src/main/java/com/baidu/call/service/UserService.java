package com.baidu.call.service;

import com.baidu.call.pojo.UserAreaVo;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    Msg addUser(UserAreaVo userAreaVo);

    Msg deleteUser(Long userId);

    Msg updateUser(Long userId,UserAreaVo userAreaVo);

    Pager queryUser(Pager pager);

    Msg findByUserId(Long userId);

//    //域账户查询用户信息
//    Msg getUserInfoOne(String userName);
//
//    SelectText findUserByUsernameUic(String username);

    //获取登录用户的信息
    Msg getLoginUserInfo(HttpServletRequest request);

}
