package com.baidu.call.service;

import com.baidu.call.model.Area;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface AreaService {
    Msg addArea(Area area);
    Msg deleteArea(Long areaId);
    Msg updateArea(Long areaId,Area area);
    Pager queryArea(Pager pager);
    Msg findByAreaId(Long areaId);
}
