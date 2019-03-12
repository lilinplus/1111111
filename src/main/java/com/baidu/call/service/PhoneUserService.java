package com.baidu.call.service;

import com.baidu.call.model.PhoneUser;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface PhoneUserService {

    Msg addPhoneUser(PhoneUser phoneUser);

    Msg deletePhoneUser(Long phoneUserId);

    Msg updatePhoneUser(Long phoneUserId,PhoneUser phoneUser);

    Pager queryPhoneUser(Pager pager);

    Msg findByPhoneUserId(Long phoneUserId);

}
