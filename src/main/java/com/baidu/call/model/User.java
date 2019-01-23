package com.baidu.call.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;

//用户表
@Entity
@Table(name = "call_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;//用户id

    @Column(name = "user_area_id")
    private Long userAreaId;//所在区域id

    @Column(name = "user_name")
    @Size(max = 50,message = "长度不得超过50")
    private String userName;//域用户名

    @Column(name = "user_role")
    @Size(max = 50,message = "长度不得超过50")
    private String userRole;//角色

}
