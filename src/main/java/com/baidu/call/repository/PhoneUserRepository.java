package com.baidu.call.repository;

import com.baidu.call.model.PhoneUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhoneUserRepository extends CrudRepository<PhoneUser,Long> {

    PhoneUser findByPhoneUserId(Long phoneUserId);

    List<PhoneUser> findByUserName(String userName);

}
