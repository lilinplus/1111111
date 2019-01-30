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

    @Column(name = "phone_name")
    @Size(max = 50,message = "长度不得超过50")
    private String phoneName;//话机名

}
