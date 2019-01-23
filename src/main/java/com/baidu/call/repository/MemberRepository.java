package com.baidu.call.repository;

import com.baidu.call.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member,Long> {

}
