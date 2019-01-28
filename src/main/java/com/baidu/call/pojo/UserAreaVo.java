package com.baidu.call.pojo;

import com.baidu.call.model.User;
import lombok.Data;

@Data
public class UserAreaVo {

//    private Long userId;//用户id
//
//    private Long userAreaId;//所在区域id
//
//    private String userName;//域用户名
//
//    private String userRole;//角色
    private User user;

    private Long [] areaId;//负责区域Id

}
