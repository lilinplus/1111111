package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.PhoneUser;
import com.baidu.call.repository.PhoneUserRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.PhoneUserService;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.ValidatorUtil;
import com.baidu.call.utils.page.dtgrid.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    //添加用户使用话机的时间段
    @Override
    public Msg addPhoneUser(PhoneUser phoneUser) {
        Msg msg = new Msg(false,"添加失败");
        try {
            String userName=phoneUser.getUserName();
            Long phoneNumId=phoneUser.getPhoneNumId();
            Long phoneStarttime=phoneUser.getPhoneStarttime();
            Long phoneEndtime=phoneUser.getPhoneEndtime();
            if(userName!=null && !"".equals(userName) && phoneNumId!=null && !"".equals(phoneNumId) && phoneStarttime!=null && !"".equals(phoneStarttime) && phoneEndtime!=null && !"".equals(phoneEndtime)){
                phoneUserRepository.save(phoneUser);
                msg.setSuccess(true);
                msg.setMsg("添加成功");
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
        Long phoneNumId=phoneUser.getPhoneNumId();
        Long phoneStarttime=phoneUser.getPhoneStarttime();
        Long phoneEndtime=phoneUser.getPhoneEndtime();
        if(userName!=null && !"".equals(userName) && phoneNumId!=null && !"".equals(phoneNumId) && phoneStarttime!=null && !"".equals(phoneStarttime) && phoneEndtime!=null && !"".equals(phoneEndtime)){
            phoneUserRepository.save(phoneUser);
            msg.setSuccess(true);
            msg.setMsg("更新成功");
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
            String sql = "SELECT cpu.phone_user_id phoneUserId,cu.user_name userName," +
                    "cp.phone_name phoneName,cpu.phone_starttime phoneStarttime," +
                    "cpu.phone_endtime phoneEndtime FROM call_phone_user cpu INNER JOIN " +
                    "call_phone cp ON cpu.phone_num_id=cp.phone_id INNER JOIN call_user cu ON " +
                    "cpu.user_name=cu.user_name WHERE 1=1";
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
            Map returnMap = getPageInfo(sql,page,size);
            if ((size != null && size != 0) && (page != null && page != 0)) {
                sql = sql + " limit " + (page - 1) * size + "," + size;
            } else {
                page = 1;
                size = 10;
                sql = sql + " limit 0,10";
            }
            List retVal = commonService.findInfoByNativeSQL(sql);
            pager.setExhibitDatas(retVal);
            pager.setPageSize(size);
            pager.setNowPage(page);
            pager.setPageCount(Integer.parseInt(returnMap.get("totalPages").toString()));
            pager.setRecordCount(Integer.parseInt(returnMap.get("totalElements").toString()));
            logger.info(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }

    public Map getPageInfo(String sql1 , Integer pageNum, Integer pageSize) {
        String sql2 = "select count(*) from (" + sql1 + ") t";
        Map map = new HashMap();
        List queryList = commonService.findInfoByNativeSQL(sql2);
        int totalElements = 0;
        int totalPages = 0;
        if (queryList.size() > 0) {
            totalElements = (int) JSONObject.parseObject(JSON.toJSONString(queryList.get(0))).get("count(*)");
            if (pageSize == 0) {
                pageSize = 1;
            }
            if (totalElements % pageSize == 0) {
                totalPages = totalElements / pageSize;
            } else {
                totalPages = totalElements / pageSize + 1;
            }
        }
        if(pageNum == null){
            pageNum = 1;
        }
        map.put("totalElements", totalElements);
        map.put("totalPages", totalPages);
        map.put("size", pageSize);
        map.put("page", pageNum);
        return map;
    }
}