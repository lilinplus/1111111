package com.baidu.call.repository;

import com.baidu.call.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByUserName(String userName);

    User findByUserId(Long userId);

}
