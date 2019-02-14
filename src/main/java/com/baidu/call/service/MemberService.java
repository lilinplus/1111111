package com.baidu.call.service;

import com.baidu.call.model.Member;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface MemberService {

    Msg addMember(Member member);

    Msg deleteMember(Long memberId);

    //Msg updateMember(Long memberId,Member member);

    Pager queryMember(Pager pager);

}
