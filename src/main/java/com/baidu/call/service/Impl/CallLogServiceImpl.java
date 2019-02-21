package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.*;
import com.baidu.call.repository.*;
import com.baidu.call.service.CallLogService;
import com.baidu.call.service.CommonService;
import com.baidu.call.utils.GetUuapUser;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;
import com.baidu.call.utils.page.dtgrid.Sort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class CallLogServiceImpl implements CallLogService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAreaRepository userAreaRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private PhoneUserRepository phoneUserRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    //查询当前用户录音信息
    @Override
    public Pager queryCallLog(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String userName= GetUuapUser.GetUser();
            if(userName==null){
                return pager;
            }
            User user=userRepository.findByUserName(userName);
            if(user==null){
                return pager;
            }

            String sql="SELECT * FROM t_call_log WHERE 1=1 ";
            List<PhoneUser> phoneUserList=phoneUserRepository.findByUserName(user.getUserName());
            for(int i=0;i<phoneUserList.size();i++){
                Long starttime=phoneUserList.get(i).getPhoneStarttime();
                Long endtime=phoneUserList.get(i).getPhoneEndtime();
                sql=sql+"and unix_timestamp(start_time)>= "+starttime+" and unix_timestamp(end_time)<= "+endtime ;
                if(phoneUserList.size()-i>1){
                    sql=sql+" or ";
                }
            }

            if(parameters != null ){
                Set<String> set=parameters.keySet();
                for(String key:set)
                {
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

    //查询用户录音信息
    @Override
    public Pager queryUserCallLog(Pager pager, String userName) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            if(userName==null){
                return pager;
            }
            User user=userRepository.findByUserName(userName);
            if(user==null){
                return pager;
            }

            String sql="SELECT * FROM t_call_log WHERE 1=1 ";
            List<PhoneUser> phoneUserList=phoneUserRepository.findByUserName(user.getUserName());
            for(int i=0;i<phoneUserList.size();i++){
                Long starttime=phoneUserList.get(i).getPhoneStarttime();
                Long endtime=phoneUserList.get(i).getPhoneEndtime();
                sql=sql+"and unix_timestamp(start_time)>= "+starttime+" and unix_timestamp(end_time)<= "+endtime ;
                if(phoneUserList.size()-i>1){
                    sql=sql+" or ";
                }
            }

            if(parameters != null ){
                Set<String> set=parameters.keySet();
                for(String key:set)
                {
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

    //查询当前用户负责的区域
    @Override
    public Msg queryUserArea() {
        Msg msg = new Msg();
        try {
            String userName= GetUuapUser.GetUser();//获取当前用户
            if(userName==null){
                return msg;
            }
            List<UserArea> userAreaList=userAreaRepository.findByUserName(userName);
            if(userAreaList==null){
                return msg;
            }
            List list=new ArrayList();
            for(int i=0;i<userAreaList.size();i++){
                Long areaId=userAreaList.get(i).getAreaId();
                Area area=areaRepository.findByAreaId(areaId);
                String areaName=area.getAreaName();
                list.add(areaName);
            }
            msg.setObj(list);
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
    }

    //查询该区域下所有用户
    @Override
    public Msg queryAreaMember(String areaName) {
        Msg msg = new Msg();
        try {
            Area area=areaRepository.findByAreaName(areaName);
            if(area==null){
                return msg;
            }
            List<User> user=userRepository.findByUserAreaId(area.getAreaId());
            if(user==null){
                return msg;
            }
            List list=new ArrayList();
            for(int i=0;i<user.size();i++){
                list.add(user.get(i).getUserName());
            }
            msg.setObj(list);
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
    }

    //查询当前用户负责的组
    @Override
    public Msg queryUserGroup() {
        Msg msg = new Msg();
        try {
            String userName= GetUuapUser.GetUser();//获取当前用户
            if(userName==null){
                return msg;
            }
            List<Group> groupList=groupRepository.findByGroupPerson(userName);
            if(groupList==null){
                return msg;
            }
            List list=new ArrayList();
            for(int i=0;i<groupList.size();i++){
                list.add(groupList.get(i).getGroupName());
            }
            msg.setObj(list);
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
    }

    //查询分组成员
    @Override
    public Msg queryGroupMember(String groupName) {
        Msg msg=new Msg();
        try {
            Group group=groupRepository.findByGroupName(groupName);
            if(group==null){
                return msg;
            }
            List<GroupUser> groupUserList=groupUserRepository.findByGroupId(group.getGroupId());
            if(groupUserList==null){
                return msg;
            }
            List list=new ArrayList();
            for(int i=0;i<groupUserList.size();i++){
                list.add(groupUserList.get(i).getUserName());
            }
            msg.setObj(list);
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
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
