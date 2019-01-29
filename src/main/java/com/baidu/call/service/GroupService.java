package com.baidu.call.service;

import com.baidu.call.model.Group;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface GroupService {

    Msg addGroup(Group group);

    Msg deleteGroup(Long groupId);

    Msg updateGroup(Long groupId,Group group);

    Pager queryGroup(Pager pager);

}
