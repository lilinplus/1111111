package com.baidu.call.repository;

import com.baidu.call.model.Area;
import org.springframework.data.repository.CrudRepository;

public interface AreaRepository extends CrudRepository<Area,Long> {

    Area findByAreaName(String areaName);

    Area findByAreaId(Long areaId);

}
