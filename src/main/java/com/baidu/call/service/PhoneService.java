package com.baidu.call.service;

import com.baidu.call.model.Phone;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;

public interface PhoneService {

    Msg addPhone(Phone phone);

    Msg deletePhone(Long phoneId);

    Msg updatePhone(Long phoneId,Phone phone);

    Pager queryPhone(Pager pager);

    Msg findByPhoneId(Long phoneId);

}
