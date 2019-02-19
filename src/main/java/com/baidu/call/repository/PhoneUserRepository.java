package com.baidu.call.repository;

import com.baidu.call.model.PhoneUser;
import org.springframework.data.repository.CrudRepository;

public interface PhoneUserRepository extends CrudRepository<PhoneUser,Long> {

    PhoneUser findByPhoneUserId(Long phoneUserId);

}
