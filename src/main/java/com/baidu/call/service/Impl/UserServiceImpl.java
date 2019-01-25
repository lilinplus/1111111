package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.User;
import com.baidu.call.repository.UserRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.UserService;
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
public class UserServiceImpl implements UserService {

    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserRepository userRepository;

    //添加用户
    @Override
    public Msg addUser(User user) {
        Msg msg = new Msg(false, "添加失败");
        try {
            if(user.getUserName()!=null && !"".equals(user.getUserName()) && user.getUserRole()!=null && !"".equals(user.getUserRole())){
                if(user.getUserAreaId()!=null && !"".equals(user.getUserAreaId())){
                    User user1=userRepository.findByUserName(user.getUserName());
                    if(user1==null){
                        userRepository.save(user);
                        msg.setSuccess(true);
                        msg.setMsg("添加成功");
                    }else {
                        msg.setMsg("用户名已存在");
                        return msg;
                    }
                }else {
                    msg.setMsg("区域不能为空");
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
    public Msg deleteUser(Long userId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            User user=userRepository.findByUserId(userId);
            if(user==null){
                msg.setMsg("用户不存在");
                return msg;
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
    public Msg updateUser(Long userId, User user) {
        Msg msg = new Msg(false, "修改失败");
        try {
            List list = ValidatorUtil.validateList(user);
            if(list != null && list.size() > 0){
                msg.setMsg(list.get(0).toString());
                return msg;
            }
            if(!userId.toString().equals(userRepository.findByUserId(userId).getUserId().toString())){
                msg.setMsg("信息错误，请检查更新的信息");
                return msg;
            }
            if(userRepository.findByUserId(userId)==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            if(user.getUserName()!=null && !"".equals(user.getUserName()) && user.getUserRole()!=null && !"".equals(user.getUserRole())){
                if(user.getUserAreaId()!=null && !"".equals(user.getUserAreaId())){
                    User user1=userRepository.findByUserName(user.getUserName());
                    if(user1==null || user1.getUserId()==userId){
                        userRepository.save(user);
                        msg.setSuccess(true);
                        msg.setMsg("修改成功");
                    }else {
                        msg.setMsg("域用户已存在");
                    }
                }else {
                    msg.setMsg("区域不能为空");
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
            String sql = "select * from call_user";
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
                    map.put("userId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("user_id"));
                    map.put("userAreaId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("user_area_id"));
                    map.put("userName",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("user_name"));
                    map.put("userRole",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("user_role"));
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
