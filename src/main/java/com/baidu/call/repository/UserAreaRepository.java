package com.baidu.call.repository;

import com.baidu.call.model.UserArea;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAreaRepository extends CrudRepository<UserArea,Long> {

    void deleteByUserName(String userName);

    void deleteByAreaId(Long areaId);

    List<UserArea> findByUserName(String userName);

    List<UserArea> findByAreaId(Long areaId);

}
