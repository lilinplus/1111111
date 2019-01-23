package com.baidu.call.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;

//分组表
@Entity
@Table(name = "call_group")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;//id

    @Column(name = "group_name")
    @Size(max = 50,message = "长度不得超过50")
    private String groupName;//分组名

}
