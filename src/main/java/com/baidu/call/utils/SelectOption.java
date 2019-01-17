package com.baidu.call.utils;

import lombok.Data;

/**
 * Created by chenyafei01_sh on 2018/10/9.
 */
@Data
public class SelectOption {

    private long id;

    private String name;

    public SelectOption() {
    }

    public SelectOption(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
