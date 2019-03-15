package com.baidu.call.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

//用户区域表
@Entity
@Table(name = "call_user_area")
@Data
public class UserArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAreaId;//id

    @Column(name = "area_id")
    @ApiModelProperty(value = "区域id")
    private Long areaId;//区域id

    @Column(name = "user_name")
    @Size(max = 50,message = "长度不得超过50")
    @ApiModelProperty(value = "域用户名")
    private String userName;//域用户名

}
