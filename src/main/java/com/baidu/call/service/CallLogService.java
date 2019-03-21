package com.baidu.call.service;

import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface CallLogService {

    Pager queryCallLog(Pager pager);//查询用户录音信息

    //Pager queryUserCallLog(Pager pager,String userName);//查询用户录音信息

    Msg queryUserArea();//查询当前用户负责的区域

    Msg queryAreaMember(String areaName);//查询该区域下所有用户

    Msg queryUserGroup();//查询当前用户负责的组

    Msg queryGroupMember(String groupName);//查询分组成员

}
