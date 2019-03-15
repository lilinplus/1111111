package com.baidu.call.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//话机用户表
@Entity
@Table(name = "call_phone_user")
@Data
public class PhoneUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneUserId;//id

    @Column(name = "user_name")
    @Size(max = 50,message = "长度不得超过50")
    @ApiModelProperty(value = "域用户名")
    private String userName;//域用户名

    @Column(name = "phone_num_id")
    @ApiModelProperty(value = "话机id")
    private Long phoneNumId;//话机id

    @Column(name = "phone_starttime")
    @ApiModelProperty(value = "开始时间")
    private Long phoneStarttime;//开始时间

    @Column(name = "phone_endtime")
    @ApiModelProperty(value = "结束时间")
    private Long phoneEndtime;//结束时间

}
