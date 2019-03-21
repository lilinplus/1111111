package com.baidu.call.service.Impl;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    //查询当前用户录音信息
    @Override
    public Pager queryCallLog(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String, Object> parameters = pager.getParameters();
        List<Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String userName = GetUuapUser.GetUser();
            if (parameters != null) {
                if (parameters.containsKey("userName")) {
                    if (parameters.get("userName") != null) {
                        userName = parameters.get("userName").toString();
                    }
                }
            }
            List list = new ArrayList();
            if (userName == null) {
                pager.setExhibitDatas(list);
                return pager;
            }

            User user = userRepository.findByUserName(userName);
            if (user == null) {
                pager.setExhibitDatas(list);
                return pager;
            }

            String sql = "SELECT * FROM t_call_log WHERE 1=1 ";
            List<PhoneUser> phoneUserList = phoneUserRepository.findByUserName(user.getUserName());
            if (phoneUserList.size() == 0) {
                pager.setExhibitDatas(list);
                return pager;
            }
            for (int i = 0; i < phoneUserList.size(); i++) {
                Long starttime = phoneUserList.get(i).getPhoneStarttime();
                Long endtime = phoneUserList.get(i).getPhoneEndtime();
                String phoneName = phoneUserList.get(i).getPhoneName();
                if (i == 0) {
                    sql = sql + " and ";
                }
                sql = sql + " unix_timestamp(start_time)*1000 >= " + starttime + " and unix_timestamp(end_time)*1000 <= " + endtime + " and call_src = " + phoneName;

                if(parameters != null){
                    if (parameters.containsKey("startTime") && parameters.get("startTime") != null) {
                        sql = sql + " and unix_timestamp(start_time)*1000 >= " + parameters.get("startTime").toString();
                    }
                    if (parameters.containsKey("endTime") && parameters.get("endTime") != null) {
                        sql = sql + " and unix_timestamp(end_time)*1000 <= " + parameters.get("endTime").toString();
                    }
                    if (parameters.containsKey("accountPinCode") && parameters.get("accountPinCode") != null) {
                        sql = sql + " and account_pin_code like '%" + parameters.get("accountPinCode").toString() + "%'";
                    }
                    if (parameters.containsKey("callerId") && parameters.get("callerId") != null) {
                        sql = sql + " and caller_id like '%" + parameters.get("callerId").toString() + "%'";
                    }
                    if (parameters.containsKey("callDestNum") && parameters.get("callDestNum") != null) {
                        sql = sql + " and call_dest_num like '%" + parameters.get("callDestNum").toString() + "%'";
                    }
                    if (parameters.containsKey("trunk") && parameters.get("trunk") != null) {
                        sql = sql + " and trunk like '%" + parameters.get("trunk").toString() + "%'";
                    }
                    if (parameters.containsKey("callType") && parameters.get("callType") != null) {
                        sql = sql + " and call_type like '%" + parameters.get("callType").toString() + "%'";
                    }
                }

                if (phoneUserList.size() - i > 1) {
                    sql = sql + " or ";
                }
            }

//            if (parameters != null) {
//                Set<String> set = parameters.keySet();
//                for (String key : set) {
//                    PropertyFieldConvertor pfc=new PropertyFieldConvertor();
//                    String str = pfc.propertyToField(key);
//                    if(str.startsWith("start")){
//                        sql = sql + " and " + str + " >= "+parameters.get(key);
//                    }else if(str.startsWith("end")){
//                        sql = sql + " and " + str + " <= "+parameters.get(key);
//                    }else {
//                        sql = sql + " and " + str + " like '%"+ parameters.get(key)+"%'";
//                    }
//                }
//            }
            List retVal1 = commonService.findInfoByNativeSQL(sql);
            if (orderBy != null) {
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
            for (int i = 0; i < retVal.size(); i++) {
                Map map = (Map) retVal.get(i);
                String callType = (String) map.get("call_type");
                if (callType.equals("internal")) {
                    map.put("call_type", "内部");
                } else if (callType.equals("outgoing")) {
                    map.put("call_type", "呼出");
                } else if (callType.equals("incoming")) {
                    map.put("call_type", "呼入");
                }
            }
            int pageCount = 0;//总页数
            int recordCount = retVal1.size();//总记录数
            if (recordCount % size == 0) {
                pageCount = recordCount / size;
            } else {
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

//    //查询用户录音信息
//    @Override
//    public Pager queryUserCallLog(Pager pager, String userName) {
//        Integer page = pager.getNowPage();
//        Integer size = pager.getPageSize();
//        Map<String, Object> parameters = pager.getParameters();
//        List<Sort> orderBy = pager.getAdvanceQuerySorts();
//        try {
//            if (userName == null) {
//                return pager;
//            }
//            User user = userRepository.findByUserName(userName);
//            if (user == null) {
//                return pager;
//            }
//
//            String sql = "SELECT * FROM t_call_log WHERE 1=1 ";
//            List<PhoneUser> phoneUserList = phoneUserRepository.findByUserName(user.getUserName());
//            for (int i = 0; i < phoneUserList.size(); i++) {
//                Long starttime = phoneUserList.get(i).getPhoneStarttime();
//                Long endtime = phoneUserList.get(i).getPhoneEndtime();
//                String phoneName = phoneUserList.get(i).getPhoneName();
//                if (i == 0) {
//                    sql = sql + " and ";
//                }
//                sql = sql + " unix_timestamp(start_time)>= " + starttime + " and unix_timestamp(end_time)<= " + endtime + " and call_src = " + phoneName;
//                if (phoneUserList.size() - i > 1) {
//                    sql = sql + " or ";
//                }
//            }
//
//            if (parameters != null) {
//                Set<String> set = parameters.keySet();
//                for (String key : set) {
//                    sql = sql + " and " + propertyToField(key) + " like '%" + parameters.get(key) + "%'";
//                }
//            }
//            if (orderBy != null) {
//                sql = sql + " order by " + propertyToField(orderBy.get(0).getField()) + " " + orderBy.get(0).getLogic();
//            }
//            if ((size != null && size != 0) && (page != null && page != 0)) {
//                sql = sql + " limit " + (page - 1) * size + "," + size;
//            } else {
//                page = 1;
//                size = 10;
//                sql = sql + " limit 0,10";
//            }
//            List retVal = commonService.findInfoByNativeSQL(sql);
//            int pageCount = 0;//总页数
//            int recordCount = retVal.size();//总记录数
//            if (recordCount % size == 0) {
//                pageCount = recordCount / size;
//            } else {
//                pageCount = recordCount / size + 1;
//            }
//            pager.setExhibitDatas(retVal);
//            pager.setPageSize(size);
//            pager.setNowPage(page);
//            pager.setPageCount(pageCount);
//            pager.setRecordCount(recordCount);
//            logger.info(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pager;
//    }

    //查询当前用户负责的区域
    @Override
    public Msg queryUserArea() {
        Msg msg = new Msg(false, "查询失败!");
        try {
            String userName = GetUuapUser.GetUser();//获取当前用户
            if (userName == null) {
                msg.setMsg("用户不存在!");
                return msg;
            }
            List<UserArea> userAreaList = userAreaRepository.findByUserName(userName);
            if (userAreaList.size() == 0) {
                msg.setMsg("当前用户没有负责区域!");
                return msg;
            }

            List list = new ArrayList();
            for (int i = 0; i < userAreaList.size(); i++) {
                Long areaId = userAreaList.get(i).getAreaId();
                Area area = areaRepository.findByAreaId(areaId);
                String areaName = area.getAreaName();
                Map map = new HashMap();
                map.put("areaId", areaId);
                map.put("areaName", areaName);
                list.add(map);
            }
            msg.setObj(list);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

    //查询该区域下所有用户
    @Override
    public Msg queryAreaMember(String areaName) {
        Msg msg = new Msg(false, "查询失败!");
        try {
            Area area = areaRepository.findByAreaName(areaName);
            if (area == null) {
                msg.setMsg("区域不存在!");
                return msg;
            }
            List<User> user = userRepository.findByUserAreaId(area.getAreaId());
            if (user.size() == 0) {
                msg.setMsg("该区域下没有用户!");
                return msg;
            }
            List list = new ArrayList();
            for (int i = 0; i < user.size(); i++) {
                String userName = user.get(i).getUserName();
                Long userId = user.get(i).getUserId();
                Map map = new HashMap();
                map.put("userId", userId);
                map.put("userName", userName);
                list.add(map);
            }
            msg.setObj(list);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

    //查询当前用户负责的组
    @Override
    public Msg queryUserGroup() {
        Msg msg = new Msg(false, "查询失败!");
        try {
            String userName = GetUuapUser.GetUser();//获取当前用户
            if (userName == null) {
                msg.setMsg("用户不存在!");
                return msg;
            }
            List<Group> groupList = groupRepository.findByGroupPerson(userName);
            if (groupList.size() == 0) {
                msg.setMsg("该用户没有指定负责的分组!");
                return msg;
            }
            List list = new ArrayList();
            for (int i = 0; i < groupList.size(); i++) {
                String groupName = groupList.get(i).getGroupName();
                Long groupId = groupList.get(i).getGroupId();
                Map map = new HashMap();
                map.put("groupId", groupId);
                map.put("groupName", groupName);
                list.add(map);
            }
            msg.setObj(list);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

    //查询分组成员
    @Override
    public Msg queryGroupMember(String groupName) {
        Msg msg = new Msg(false, "查询失败!");
        try {
            Group group = groupRepository.findByGroupName(groupName);
            if (group == null) {
                msg.setMsg("分组不存在!");
                return msg;
            }
            List<GroupUser> groupUserList = groupUserRepository.findByGroupId(group.getGroupId());
            if (groupUserList.size() == 0) {
                msg.setMsg("该分组下没有成员!");
                return msg;
            }
            List list = new ArrayList();
            for (int i = 0; i < groupUserList.size(); i++) {
                String userName = groupUserList.get(i).getUserName();
                if (userName == null) {
                    msg.setMsg("用户不存在!");
                    return msg;
                }
                User user = userRepository.findByUserName(userName);
                Map map = new HashMap();
                map.put("userId", user.getUserId());
                map.put("userName", userName);
                list.add(map);
            }
            msg.setObj(list);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

}
