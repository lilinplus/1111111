package com.baidu.call.repository;

import com.baidu.call.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member,Long> {

    void deleteByMemberGroupId(Long groupId);

    List<Member> findByMemberGroupId(Long groupId);

    Member findByMemberId(Long memberId);

}
