package com.baidu.call.repository;

import com.baidu.call.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUserName(String userName);

    User findByUserId(Long userId);

    List<User> findByUserAreaId(Long areaId);

    @Query(value = "select cu from User cu where cu.userRole='普通用户'")
    List<User> findUserByUserRole();

    @Query(value = "select cu from User cu where cu.userRole='总经理'")
    List<User> findByUserRole();

}
