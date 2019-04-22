package com.baidu.call.service.Impl;

import com.baidu.call.model.PhoneUser;
import com.baidu.call.model.User;
import com.baidu.call.repository.PhoneUserRepository;
import com.baidu.call.repository.UserRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.PhoneUserService;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.ValidatorUtil;
import com.baidu.call.utils.page.dtgrid.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class PhoneUserServiceImpl implements PhoneUserService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private PhoneUserRepository phoneUserRepository;

    @Autowired
    private UserRepository userRepository;

    //添加用户使用话机的时间段
    @Override
    public Msg addPhoneUser(PhoneUser phoneUser) {
        Msg msg = new Msg(false,"添加失败");
        try {
            String userName=phoneUser.getUserName();
            if(phoneUser.getPhoneName() == null){
                msg.setMsg("话机名不能为空");
                return msg;
            }
            String phoneName=phoneUser.getPhoneName().trim();//话机名
            Long phoneStarttime=phoneUser.getPhoneStarttime();
            Long phoneEndtime=phoneUser.getPhoneEndtime();
            if(userName!=null && !"".equals(userName) && phoneName!=null && !"".equals(phoneName) && phoneStarttime!=null && !"".equals(phoneStarttime) && phoneEndtime!=null && !"".equals(phoneEndtime)){
                if(phoneEndtime>phoneStarttime){
                    User user = userRepository.findByUserName(userName);
                    if(user == null){
                        msg.setMsg("用户不存在，请在用户管理中添加");
                        return msg;
                    }
                    phoneUser.setPhoneName(phoneName);
                    phoneUserRepository.save(phoneUser);
                    msg.setSuccess(true);
                    msg.setMsg("添加成功");
                }else {
                    msg.setMsg("结束时间不能早于开始时间!");
                }
            }else {
                msg.setMsg("文本框不能为空");
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    @Override
    public Msg deletePhoneUser(Long phoneUserId) {
        Msg msg = new Msg(false,"删除失败");
        try {
            PhoneUser phoneUser=phoneUserRepository.findByPhoneUserId(phoneUserId);
            if(phoneUser==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            phoneUserRepository.deleteById(phoneUserId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

    @Override
    public Msg updatePhoneUser(Long phoneUserId, PhoneUser phoneUser) {
        Msg msg = new Msg(false,"更新失败");
        List list = ValidatorUtil.validateList(phoneUser);
        if(list != null && list.size() > 0){
            msg.setMsg(list.get(0).toString());
            return msg;
        }
        if(!phoneUserId.toString().equals(phoneUserRepository.findByPhoneUserId(phoneUserId).getPhoneUserId().toString())){
            msg.setMsg("信息错误，请检查更新的信息");
            return msg;
        }
        if(phoneUserRepository.findByPhoneUserId(phoneUserId)==null){
            msg.setMsg("信息不存在");
            return msg;
        }
        String userName=phoneUser.getUserName();
        String phoneName=phoneUser.getPhoneName();
        Long phoneStarttime=phoneUser.getPhoneStarttime();
        Long phoneEndtime=phoneUser.getPhoneEndtime();
        if(userName!=null && !"".equals(userName) && phoneName!=null && !"".equals(phoneName) && phoneStarttime!=null && !"".equals(phoneStarttime) && phoneEndtime!=null && !"".equals(phoneEndtime)){
            if(phoneEndtime>phoneStarttime){
                User user = userRepository.findByUserName(userName);
                if(user == null){
                    msg.setMsg("用户不存在，请在用户管理中添加");
                    return msg;
                }
                phoneUser.setPhoneName(phoneName);
                phoneUserRepository.save(phoneUser);
                msg.setSuccess(true);
                msg.setMsg("更新成功");
            }else {
                msg.setMsg("结束时间不能早于开始时间!");
            }
        }else {
            msg.setMsg("文本框不能为空");
        }
        return msg;
    }

    @Override
    public Pager queryPhoneUser(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "SELECT cp.phone_user_id phoneUserId,cp.phone_name phoneName," +
                    "cp.user_name userName,cp.phone_starttime phoneStarttime," +
                    "cp.phone_endtime phoneEndtime from call_phone_user cp WHERE 1=1";
            if(parameters != null ){
                Set<String> set=parameters.keySet();
                for(String key:set)
                {
                    System.out.println(key+":"+parameters.get(key));
                    sql = sql + " and " + propertyToField(key) + " like '%"+ parameters.get(key)+"%'";
                }
            }
            if(orderBy != null){
                sql = sql + " order by " + propertyToField(orderBy.get(0).getField()) + " " + orderBy.get(0).getLogic();
            }
            if ((size != null && size != 0) && (page != null && page != 0)) {
                sql = sql + " limit " + (page - 1) * size + "," + size;
            } else {
                page = 1;
                size = 10;
                sql = sql + " limit 0,10";
            }
            List retVal = commonService.findInfoByNativeSQL(sql);
            int pageCount=0;//总页数
            int recordCount=retVal.size();//总记录数
            if(recordCount % size==0){
                pageCount = recordCount / size;
            }else {
                pageCount = recordCount / size + 1;
            }
            pager.setExhibitDatas(retVal);
            pager.setPageSize(size);
            pager.setNowPage(page);
            pager.setPageCount(pageCount);
            pager.setRecordCount(recordCount);
            logger.info(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }

    //根据Id查询
    @Override
    public Msg findByPhoneUserId(Long phoneUserId) {
        Msg msg=new Msg(false,"查询失败!");
        try {
            PhoneUser phoneUser=phoneUserRepository.findByPhoneUserId(phoneUserId);
            if(phoneUser==null){
                msg.setMsg("信息不存在!");
                return msg;
            }
            msg.setObj(phoneUser);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
    }

}
