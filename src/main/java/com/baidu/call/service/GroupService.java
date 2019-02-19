package com.baidu.call.service;

import com.baidu.call.pojo.GroupUserVo;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface GroupService {

    Msg addGroup(GroupUserVo groupUserVo);

    Msg deleteGroup(Long groupId);

    Msg updateGroup(Long groupId,GroupUserVo groupUserVo);

    Pager queryGroup(Pager pager);

}
