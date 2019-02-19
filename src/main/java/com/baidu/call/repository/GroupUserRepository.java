package com.baidu.call.repository;

import com.baidu.call.model.GroupUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupUserRepository extends CrudRepository<GroupUser,Long> {

    @Modifying
    void deleteByGroupId(Long groupId);

    List<GroupUser> findByGroupId(Long groupId);

    GroupUser findByUserName(String userName);

}
