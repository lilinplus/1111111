package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.Group;
import com.baidu.call.model.GroupUser;
import com.baidu.call.model.User;
import com.baidu.call.pojo.GroupUserVo;
import com.baidu.call.repository.GroupRepository;
import com.baidu.call.repository.GroupUserRepository;
import com.baidu.call.repository.UserRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.GroupService;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class GroupServiceImpl implements GroupService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg addGroup(GroupUserVo groupUserVo) {
        Msg msg = new Msg(false, "添加失败");
        try{
            String groupName=groupUserVo.getGroup().getGroupName();
            String groupPerson=groupUserVo.getGroup().getGroupPerson();
            String[] userName=groupUserVo.getUserName();
            if(groupName!=null && !"".equals(groupName) && groupPerson!=null && !"".equals(groupPerson)){
                if(userName!=null && !"".equals(userName)){
                    Group group1=groupRepository.findByGroupName(groupName);
                    if(group1==null){
                        Group group=new Group();
                        group.setGroupName(groupName);
                        group.setGroupPerson(groupPerson);
                        groupRepository.save(group);
                        for(int i=0;i<userName.length;i++){
                            User user = userRepository.findByUserName(userName[i]);
                            if(user!=null){
                                Long groupId=group.getGroupId();
                                GroupUser groupUser=new GroupUser();
                                groupUser.setGroupId(groupId);
                                groupUser.setUserName(userName[i]);
                                groupUserRepository.save(groupUser);
                            }else {
                                msg.setMsg("所选成员不存在");
                                return msg;
                            }
                        }
                        msg.setSuccess(true);
                        msg.setMsg("添加成功");
                    }else {
                        msg.setMsg("分组名已存在");
                    }
                }else {
                    msg.setMsg("成员不能为空");
                }
            }else {
                msg.setMsg("分组名和指定人不能为空");
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    //删除分组
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg deleteGroup(Long groupId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            Group group=groupRepository.findByGroupId(groupId);
            if(group==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            groupUserRepository.deleteByGroupId(groupId);
            groupRepository.deleteById(groupId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg updateGroup(Long groupId, GroupUserVo groupUserVo) {
        Msg msg = new Msg(false, "修改失败");
        try {
            String groupName=groupUserVo.getGroup().getGroupName();//域用户
            String groupPerson=groupUserVo.getGroup().getGroupPerson();//指定人
            String[] userName=groupUserVo.getUserName();//分组成员
            Long groupId2=groupUserVo.getGroup().getGroupId();//分组id
            if(groupName!=null && !"".equals(groupName) && groupPerson!=null && !"".equals(groupPerson)){
                if(userName!=null && !"".equals(userName)) {
                    Group group1 = groupRepository.findByGroupName(groupName);
                    if (group1 == null || group1.getGroupId() == groupId) {
                        Group group=groupUserVo.getGroup();
                        groupRepository.save(group);
                        List<GroupUser> groupUserList=groupUserRepository.findByGroupId(groupId2);
                        if(groupUserList.size()>0){
                            groupUserRepository.deleteByGroupId(groupId2);
                        }

                        for(int i=0;i<userName.length;i++){
                            User user = userRepository.findByUserName(userName[i]);
                            if(user!=null){
                                GroupUser groupUser=new GroupUser();
                                groupUser.setGroupId(groupId);
                                groupUser.setUserName(userName[i]);
                                groupUserRepository.save(groupUser);
                            }else {
                                msg.setMsg("所选成员不存在!");
                            }
                        }
                        msg.setSuccess(true);
                        msg.setMsg("修改成功");
                    } else {
                        msg.setMsg("分组名已存在");
                    }
                }else {
                    msg.setMsg("成员不能为空");
                }
            }else {
                msg.setMsg("分组名不能为空");
            }
        }catch (Exception e){
            msg.setMsg("修改失败"+e);
        }
        return msg;
    }

    //查询分组信息
    @Override
    public Pager queryGroup(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select * from call_group where 1=1";
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
                    Long groupId= Long.valueOf(JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("group_id").toString());
                    Map map = new HashMap();
                    map.put("groupId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("group_id"));
                    map.put("groupName", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("group_name"));
                    map.put("groupPerson", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("group_person"));
                    List<GroupUser> groupUserList=groupUserRepository.findByGroupId(groupId);
                    if(groupUserList!=null){
                        String str[] = new String[groupUserList.size()];
                        for(int p=0;p<groupUserList.size();p++){
                            String userName=groupUserList.get(p).getUserName();
                            str[p]=userName;
                        }
                        StringBuffer permission=new StringBuffer();
                        if(str.length>0){
                            for(int m=0;m<str.length;m++){
                                if(m==0){
                                    permission.append(str[0].toString());
                                }else {
                                    permission.append(","+str[m].toString());
                                }
                            }
                        }
                        map.put("member",permission);
                    }
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

    //根据Id查询分组
    @Override
    public Msg findByGroupId(Long groupId) {
        Msg msg=new Msg(false,"查询失败!");
        try {
            Group group=groupRepository.findByGroupId(groupId);
            if(group==null){
                msg.setMsg("分组不存在!");
                return msg;
            }
            List<GroupUser> groupUserList=groupUserRepository.findByGroupId(groupId);
            Map map = new HashMap();
            if (groupUserList != null) {
                String strAreaId[] = new String[groupUserList.size()];
                List<Group> groupList = new ArrayList<Group>();
                for (int p = 0; p < groupUserList.size(); p++) {
                    strAreaId[p] = groupUserList.get(p).getUserName();
                }
                StringBuffer permission = new StringBuffer();
                if (strAreaId.length > 0) {
                    for (int m = 0; m < strAreaId.length; m++) {
                        if (m == 0) {
                            permission.append(strAreaId[0].toString());
                        } else {
                            permission.append("," + strAreaId[m].toString());
                        }
                    }
                }
                map.put("member", permission);
                map.put("groupId",group.getGroupId());
                map.put("groupName",group.getGroupName());
                map.put("groupPerson",group.getGroupPerson());
            }
            msg.setObj(map);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        }catch (Exception e){
            msg.setMsg("查询失败 "+e);
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
