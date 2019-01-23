package com.baidu.call.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;

//区域表
@Entity
@Table(name = "call_area")
@Data
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaId;//区域id

    @Column(name = "area_name")
    @Size(max = 50,message = "长度不得超过50")
    private String areaName;//区域名

    @Column(name = "area_createtime")
    private Long areaCreatetime;//创建时间

    @Column(name = "area_remark")
    @Size(max = 200,message = "长度不得超过200")
    private String areaRemarks;//备注

}
