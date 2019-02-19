package com.baidu.call.repository;

import com.baidu.call.model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group,Long> {

    Group findByGroupName(String groupName);

    Group findByGroupId(Long groupId);

    Group findByGroupPerson(String groupPerson);

}
