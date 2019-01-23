package com.baidu.call.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//分组用户表(指定用户可以查看该分组成员通话记录)
@Entity
@Table(name = "call_group_user")
@Data
public class GroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupUserId;//id

    @Column(name = "group_id")
    private Long GroupId;//分组id

    @Column(name = "user_name")
    @Size(max = 50,message = "长度不得超过50")
    private String userName;//域用户名

}
