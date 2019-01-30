package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.Phone;
import com.baidu.call.repository.PhoneRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.PhoneService;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.ValidatorUtil;
import com.baidu.call.utils.page.dtgrid.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class PhoneServiceImpl implements PhoneService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private PhoneRepository phoneRepository;

    @Override
    public Msg addPhone(Phone phone) {
        Msg msg = new Msg(false, "添加失败");
        try {
            String phoneName=phone.getPhoneName();
            if(phoneName!=null && !"".equals(phoneName)){
                Phone phone1=phoneRepository.findByPhoneName(phoneName);
                if(phone1==null){
                    phoneRepository.save(phone);
                    msg.setSuccess(true);
                    msg.setMsg("添加成功");
                }else {
                    msg.setMsg("话机号已存在");
                }
            }else {
                msg.setMsg("话机号不能为空");
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    @Override
    public Msg deletePhone(Long phoneId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            Phone phone=phoneRepository.findByPhoneId(phoneId);
            if(phone==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            phoneRepository.deleteById(phoneId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

    @Override
    public Msg updatePhone(Long phoneId, Phone phone) {
        Msg msg = new Msg(false, "修改失败");
        List list = ValidatorUtil.validateList(phone);
        if(list != null && list.size() > 0){
            msg.setMsg(list.get(0).toString());
            return msg;
        }
        if(!phoneId.toString().equals(phoneRepository.findByPhoneId(phoneId).getPhoneId().toString())){
            msg.setMsg("信息错误，请检查更新的信息");
            return msg;
        }
        if(phoneRepository.findByPhoneId(phoneId)==null){
            msg.setMsg("信息不存在");
            return msg;
        }
        String phoneName=phone.getPhoneName();
        if(phoneName!=null && !"".equals(phoneName)){
            Phone phone1=phoneRepository.findByPhoneName(phoneName);
            if(phone1==null || phone1.getPhoneId()==phoneId){
                phoneRepository.save(phone);
                msg.setSuccess(true);
                msg.setMsg("修改成功");
            }else {
                msg.setMsg("话机号已存在");
            }
        }else {
            msg.setMsg("话机号不能为空");
        }
        return msg;
    }

    @Override
    public Pager queryPhone(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select * from call_phone where 1=1";
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
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            if (retVal.size() > 0) {
                for (int j = 0; j < retVal.size(); j++) {
                    Map map = new HashMap();
                    map.put("phoneId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("phone_id"));
                    map.put("phoneName", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("phone_name"));
                    listMap.add(map);
                }
            }
            pager.setExhibitDatas(listMap);
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
