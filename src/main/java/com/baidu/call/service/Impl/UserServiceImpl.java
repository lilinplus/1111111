package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.*;
import com.baidu.call.pojo.UserAreaVo;
import com.baidu.call.repository.*;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.UserService;
import com.baidu.call.utils.GetUuapUser;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.SelectText;
import com.baidu.call.utils.page.dtgrid.Pager;
import com.baidu.uic.ws.dto.UserDTO;
import com.baidu.uic.ws.interfaces.IUserRemoteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserAreaRepository userAreaRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private IUserRemoteService uicUserRemoteService;

    //添加用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg addUser(UserAreaVo userAreaVo) {
        Msg msg = new Msg(false, "添加失败");
        try {
            String userName=userAreaVo.getUser().getUserName();//域用户
            String role=userAreaVo.getUser().getUserRole();//角色名
            Long areaId=userAreaVo.getUser().getUserAreaId();//所在区域Id
            UserDTO userDTO = uicUserRemoteService.getUserByUsername(userName);
            if(userDTO==null){
                msg.setMsg("域用户错误!");
                return msg;
            }
            if(userName!=null && !"".equals(userName) && role!=null && !"".equals(role)){
                if(areaId!=null && !"".equals(areaId)){
                    if("普通用户".equals(userAreaVo.getUser().getUserRole())){
                        User user1=userRepository.findByUserName(userName);
                        if(user1==null){
                            User user=new User();
                            user.setUserAreaId(areaId);
                            user.setUserName(userName);
                            user.setUserRole(role);
                            userRepository.save(user);
                            msg.setSuccess(true);
                            msg.setMsg("添加成功");
                        }else {
                            msg.setMsg("域用户已存在");
                            return msg;
                        }
                    }else {
                        if(userAreaVo.getAreaId()!=null && !"".equals(userAreaVo.getAreaId())){
                            User user1=userRepository.findByUserName(userName);
                            Long[] areaId2=userAreaVo.getAreaId();
                            if(user1==null){
                                User user=new User();
                                user.setUserAreaId(areaId);
                                user.setUserName(userName);
                                user.setUserRole(role);
                                userRepository.save(user);
                                for(int i=0;i<areaId2.length;i++){
                                    Area area=areaRepository.findByAreaId(areaId2[i]);
                                    if(area!=null){
                                        UserArea userArea=new UserArea();
                                        userArea.setAreaId(areaId2[i]);
                                        userArea.setUserName(userName);
                                        userAreaRepository.save(userArea);
                                    }else {
                                        msg.setMsg("所选区域不存在");
                                    }
                                }
                                msg.setSuccess(true);
                                msg.setMsg("添加成功");
                            }else {
                                msg.setMsg("域用户已存在");
                                return msg;
                            }
                        }else {
                            msg.setMsg("负责区域不能为空");
                        }
                    }
                }else {
                    msg.setMsg("所在区域不能为空");
                }
            }else {
                msg.setMsg("用户名和角色不能为空");
                return msg;
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    //删除用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg deleteUser(Long userId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            User user=userRepository.findByUserId(userId);
            if(user==null){
                msg.setMsg("用户不存在");
                return msg;
            }
            List<Group> group=groupRepository.findByGroupPerson(user.getUserName());
            if(group!=null){
                String groupName=group.get(0).getGroupName();
                msg.setMsg(groupName+"分组指定人为该用户，不能删除");
                return msg;
            }
            GroupUser groupUser=groupUserRepository.findByUserName(user.getUserName());
            if(groupUser!=null){
                Group group1=groupRepository.findByGroupId(groupUser.getGroupId());
                String groupName1=group1.getGroupName();
                msg.setMsg(groupName1+"分组成员中存在该用户，不能删除");
                return msg;
            }
            String userName=user.getUserName();
            if(userName!=null){
                userAreaRepository.deleteByUserName(userName);
            }
            userRepository.deleteById(userId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

    //修改用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Msg updateUser(Long userId,UserAreaVo userAreaVo) {
        Msg msg = new Msg(false, "修改失败");
        try {
            String userName=userAreaVo.getUser().getUserName();//域用户
            String role=userAreaVo.getUser().getUserRole();//角色名
            Long areaId=userAreaVo.getUser().getUserAreaId();//所在区域Id
            if(userName!=null && !"".equals(userName) && role!=null && !"".equals(role)){
                if(areaId!=null && !"".equals(areaId)){
                    User user1=userRepository.findByUserName(userName);
                    if(user1==null || user1.getUserId()==userId){
                        if("普通用户".equals(userAreaVo.getUser().getUserRole())){
                            List<UserArea> userAreaList=userAreaRepository.findByUserName(userName);
                            if(userAreaList.size()>0){
                                userAreaRepository.deleteByUserName(userName);
                            }
                            User user=userAreaVo.getUser();
                            userRepository.save(user);
                            msg.setSuccess(true);
                            msg.setMsg("修改成功");
                        }else {
                            if(userAreaVo.getAreaId()!=null && !"".equals(userAreaVo.getAreaId())){
                                User user=userAreaVo.getUser();
                                userRepository.save(user);
                                List<UserArea> userAreaList=userAreaRepository.findByUserName(userName);
                                if(userAreaList.size()>0){
                                    userAreaRepository.deleteByUserName(userName);
                                }
                                Long[] areaId2=userAreaVo.getAreaId();//负责区域id
                                for(int i=0;i<areaId2.length;i++){
                                    Area area=areaRepository.findByAreaId(areaId2[i]);
                                    if(area!=null){
                                        UserArea userArea=new UserArea();
                                        userArea.setAreaId(areaId2[i]);
                                        userArea.setUserName(userName);
                                        userAreaRepository.save(userArea);
                                    }else {
                                        msg.setMsg("负责区域不存在");
                                    }
                                }
                                msg.setSuccess(true);
                                msg.setMsg("修改成功");
                            }else {
                                msg.setMsg("负责区域不能为空");
                                return msg;
                            }
                        }
                    }else {
                        msg.setMsg("域用户已存在");
                    }
                }else {
                    msg.setMsg("所属区域不能为空");
                }
            }else {
                msg.setMsg("域用户和角色不能为空");
            }
        }catch (Exception e){
            msg.setMsg("修改失败"+e);
        }
        return msg;
    }

    //查询用户
    @Override
    public Pager queryUser(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select cu.user_id as userId,ca.area_name as areaName," +
                    "cu.user_name as userName,cu.user_role as userRole from call_user cu " +
                    "left join call_area ca on cu.user_area_id=ca.area_id where 1=1";
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
                    String userName = JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("userName").toString();
                    map.put("userId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("userId"));
                    map.put("userAreaName", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("areaName"));
                    map.put("userName",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("userName"));
                    map.put("userRole",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("userRole"));
                    List<UserArea> userAreaList=userAreaRepository.findByUserName(userName);
                    if (userAreaList != null) {
                        String strAreaId[] = new String[userAreaList.size()];
                        List<Area> areaList = new ArrayList<Area>();
                        for (int p = 0; p < userAreaList.size(); p++) {
                            Long riAreaId = userAreaList.get(p).getAreaId();
                            Area area = areaRepository.findByAreaId(riAreaId);
                            areaList.add(area);
                            strAreaId[p] = area.getAreaName();
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
                        map.put("areaName", permission);
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

    //根据Id查询用户
    @Override
    public Msg findByUserId(Long userId) {
        Msg msg=new Msg(false,"查询失败!");
        try {
            User user=userRepository.findByUserId(userId);
            List<UserArea> userAreaList=userAreaRepository.findByUserName(user.getUserName());
            Map map = new HashMap();
            if (userAreaList != null) {
                String strAreaId[] = new String[userAreaList.size()];
                List<Area> areaList = new ArrayList<Area>();
                for (int p = 0; p < userAreaList.size(); p++) {
                    Long riAreaId = userAreaList.get(p).getAreaId();
                    Area area = areaRepository.findByAreaId(riAreaId);
                    areaList.add(area);
                    strAreaId[p] = area.getAreaName();
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
                Area area=areaRepository.findByAreaId(user.getUserAreaId());
                map.put("areaName", permission);
                map.put("userId",user.getUserId());
                map.put("userName",user.getUserName());
                map.put("userRole",user.getUserRole());
                map.put("userAreaName",area.getAreaName());
            }
            msg.setObj(map);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        }catch (Exception e){
            msg.setMsg("查询失败"+e);
        }
        return msg;
    }

//    @Override
//    public Msg getUserInfoOne(String userName) {
//        Msg msg = new Msg();
//        try {
//            User user = userRepository.findByUserName(userName);
//            UserAreaVo userAreaVo = new UserAreaVo();
//            userAreaVo.setUser(user);
//            BeanUtils.copyProperties(user, userAreaVo);
//            if(user != null){
//                List<UserArea> userAreaList = userAreaRepository.findByUserName(userName);
//                Long [] areaId = new Long[userAreaList.size()];
//                if(userAreaList.size()>0){
//                    for (int j = 0; j < userAreaList.size(); j++) {
//                        areaId[j] = userAreaList.get(j).getAreaId();
//                    }
//                    userAreaVo.setAreaId(areaId);
//                }else{
//                    userAreaVo.setAreaId(null);
//                }
//                msg.setSuccess(true);
//                msg.setMsg("查询成功");
//                msg.setObj(userAreaVo);
//            }else{
//                msg.setMsg("该用户信息不存在");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }

//    /**
//     * 根据username精确查询uic用户
//     *
//     * @param username
//     * @return
//     */
//    @Override
//    public SelectText findUserByUsernameUic(String username) {
//        SelectText selectText = new SelectText();
//        try {
//            UserDTO userDTO = uicUserRemoteService.getUserByUsername(username);
//            if (userDTO != null) {
//                selectText.setName(userDTO.getName());
//                selectText.setUsername(userDTO.getUsername());
//                selectText.setHiAccount(userDTO.getHiNumber());
//                selectText.setPhoneNumber(userDTO.getPhoneNumber());
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return selectText;
//    }

    @Override
    public Msg getLoginUserInfo(HttpServletRequest request) {
        Msg msg = new Msg();
        msg.setSuccess(false);
        msg.setMsg("用户信息不存在!");
        try {
            String username = GetUuapUser.GetUser();
            if (username == null) {
                return msg;
            }
            User localUser = userRepository.findByUserName(username);
            if (localUser == null) {
                UserDTO userDTO = uicUserRemoteService.getUserByUsername(username);
                if (userDTO != null) {
                    User user = new User();
                    user.setUserName(userDTO.getUsername());
                    localUser = userRepository.save(user);
                    msg.setSuccess(true);
                    msg.setObj(localUser);
                    msg.setMsg("用户信息获取成功!");
                }
            } else {
                msg.setSuccess(true);
                msg.setObj(localUser);
                msg.setMsg("用户信息获取成功!");
            }
        } catch (Exception e) {
            logger.error(e);
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
