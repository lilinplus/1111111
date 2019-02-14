package com.baidu.call.repository;

import com.baidu.call.model.GroupUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface GroupUserRepository extends CrudRepository<GroupUser,Long> {

    @Modifying
    void deleteByGroupId(Long groupId);

}
