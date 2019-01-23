package com.baidu.call.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//话机表
@Entity
@Table(name = "call_phone")
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneId;//id

    @Column(name = "user_name")
    @Size(max = 50,message = "长度不得超过50")
    private String userName;//域用户名

    @Column(name = "phone_num")
    @Size(max = 100,message = "长度不得超过100")
    private String phoneNum;//话机号

    @Column(name = "phone_starttime")
    private Long phoneStarttime;//开始时间

    @Column(name = "phone_endtime")
    private Long phoneEndtime;//结束时间

}
