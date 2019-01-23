package com.baidu.call.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//分组成员表
@Entity
@Table(name = "call_member")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;//id

    @Column(name = "member_group_id")
    private Long memberGroupId;//分组id

    @Column(name = "member_name")
    @Size(max = 50,message = "长度不得超过50")
    private String memberName;//成员域用户

}
