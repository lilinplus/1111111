package com.baidu.call.service;

import java.util.List;
import java.util.Map;

/**
 * Created by v_chenyafei on 2017/5/2.
 */
public interface CommonService {
    List<Map<String, Object>> findInfoByNativeSQL(String sql);
}
