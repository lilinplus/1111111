package com.baidu.call.pojo;

import com.baidu.call.model.Group;
import lombok.Data;

@Data
public class GroupUserVo {

    private Group group;

    private String[] userName;//分组成员域用户

}
