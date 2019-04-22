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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.aspectj.AbstractTransactionAspect;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public Msg addGroup(GroupUserVo groupUserVo) {
        Msg msg = new Msg(false, "添加失败");
        try{
            Group group=groupUserVo.getGroup();
            if(group == null){
                msg.setMsg("分组名和指定人不能为空");
                return msg;
            }
            String[] userName=groupUserVo.getUserName();
            if(group.getGroupName()!=null && !"".equals(group.getGroupName()) && group.getGroupPerson()!=null && !"".equals(group.getGroupPerson())){
                if(userName!=null && !"".equals(userName)){
                    Group group1=groupRepository.findByGroupName(group.getGroupName());
                    if(group1==null){
                        User user1 = userRepository.findByUserName(group.getGroupPerson());
                        if(user1 == null){
                            msg.setMsg("指定人不存在，请在用户管理中添加");
                            return msg;
                        }
                        Group group2=new Group();
                        group2.setGroupName(group.getGroupName());
                        group2.setGroupPerson(group.getGroupPerson());
                        groupRepository.save(group2);
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
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
        }
        return msg;
    }

    //删除分组
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
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
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
        }
        return msg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Msg updateGroup(Long groupId, GroupUserVo groupUserVo) {
        Msg msg = new Msg(false, "修改失败");
        try {
            Group group=groupUserVo.getGroup();
            if(group == null){
                msg.setMsg("分组名和指定人不能为空");
                return msg;
            }
            String[] userName=groupUserVo.getUserName();//分组成员
            Long groupId2=groupUserVo.getGroup().getGroupId();//分组id
            if(group.getGroupName()!=null && !"".equals(group.getGroupName()) && group.getGroupPerson()!=null && !"".equals(group.getGroupPerson())){
                if(userName!=null && !"".equals(userName)) {
                    Group group1 = groupRepository.findByGroupName(group.getGroupName());
                    if (group1 == null || group1.getGroupId() == groupId) {
                        User user1 = userRepository.findByUserName(group.getGroupPerson());
                        if(user1 == null){
                            msg.setMsg("指定人不存在，请在用户管理中添加");
                            return msg;
                        }
                        Group group2=groupUserVo.getGroup();
                        groupRepository.save(group2);
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
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
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
            pager.setPageCount(pageCount);
            pager.setRecordCount(recordCount);
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
                List list = new ArrayList();
                for (int p = 0; p < groupUserList.size(); p++) {
                    String userName=groupUserList.get(p).getUserName();
                    list.add(userName);
                }
                StringBuffer permission = new StringBuffer();
                if (list.size() > 0) {
                    for (int m = 0; m < list.size(); m++) {
                        if (m == 0) {
                            permission.append(list.get(0).toString());
                        } else {
                            permission.append("," + list.get(m).toString());
                        }
                    }
                }
                map.put("userName", permission);
                map.put("group", group);
            }
            msg.setObj(map);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        }catch (Exception e){
            msg.setMsg("查询失败 "+e);
        }
        return msg;
    }

}
