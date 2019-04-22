package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.Area;
import com.baidu.call.model.User;
import com.baidu.call.model.UserArea;
import com.baidu.call.repository.AreaRepository;
import com.baidu.call.repository.UserAreaRepository;
import com.baidu.call.repository.UserRepository;
import com.baidu.call.service.AreaService;
import com.baidu.call.service.CommonService;
import com.baidu.call.utils.GetUuapUser;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.ValidatorUtil;
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
public class AreaServiceImpl implements AreaService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAreaRepository userAreaRepository;

    //添加区域
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Msg addArea(Area area) {
        Msg msg = new Msg(false, "添加失败");
        try {
            if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
                Area area1 = areaRepository.findByAreaName(area.getAreaName());
                if (area1 == null) {
                    area.setAreaCreatetime(new Date().getTime());
                    areaRepository.save(area);
                    List<User> userList = userRepository.findByUserRole();
                    if(userList!=null){
                        List<Area> areaList = (List<Area>) areaRepository.findAll();
                        for(int i=0;i<userList.size();i++){
                            userAreaRepository.deleteByUserName(userList.get(i).getUserName());
                            for(int a=0;a<areaList.size();a++){
                                UserArea userArea = new UserArea();
                                userArea.setUserName(userList.get(i).getUserName());
                                userArea.setAreaId(areaList.get(a).getAreaId());
                                userAreaRepository.save(userArea);
                            }
                        }
                    }
                    msg.setSuccess(true);
                    msg.setMsg("添加成功");
                } else {
                    msg.setMsg("区域名已存在");
                    return msg;
                }
            } else {
                msg.setMsg("区域名不能为空");
                return msg;
            }
        } catch (Exception e) {
            msg.setMsg("添加失败" + e);
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
        }
        return msg;
    }

    //删除区域
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Msg deleteArea(Long areaId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            Area area = areaRepository.findByAreaId(areaId);
            if (area == null) {
                msg.setMsg("信息不存在");
                return msg;
            }
            List<User> userList = userRepository.findByUserAreaId(areaId);
            if(userList.size() > 0){
                msg.setMsg("区域有引用，不能删除");
                return msg;
            }
            userAreaRepository.deleteByAreaId(areaId);
            areaRepository.deleteById(areaId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        } catch (Exception e) {
            msg.setMsg("删除失败" + e);
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
        }
        return msg;
    }

    //修改区域信息
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Msg updateArea(Long areaId, Area area) {
        Msg msg = new Msg(false, "修改失败");
        try {
            List list = ValidatorUtil.validateList(area);
            if (list != null && list.size() > 0) {
                msg.setMsg(list.get(0).toString());
                return msg;
            }
            if (!areaId.toString().equals(areaRepository.findByAreaId(areaId).getAreaId().toString())) {
                msg.setMsg("信息错误，请检查更新的信息");
                return msg;
            }
            if (areaRepository.findByAreaId(areaId) == null) {
                msg.setMsg("信息不存在");
                return msg;
            }
            if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
                Area area1 = areaRepository.findByAreaName(area.getAreaName());
                if (area1 == null || area1.getAreaId() == areaId) {
                    areaRepository.save(area);
                    msg.setSuccess(true);
                    msg.setMsg("修改成功");
                } else {
                    msg.setMsg("区域名已存在");
                }
            } else {
                msg.setMsg("区域名不能为空");
            }
        } catch (Exception e) {
            msg.setMsg("修改失败" + e);
            AbstractTransactionAspect.currentTransactionStatus().setRollbackOnly();
        }
        return msg;
    }

    //查询区域信息
    @Override
    public Pager queryArea(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String, Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select * from call_area where 1=1";
            if (parameters != null) {
                Set<String> set = parameters.keySet();
                for (String key : set) {
                    System.out.println(key + ":" + parameters.get(key));
                    sql = sql + " and " + propertyToField(key) + " like '%" + parameters.get(key) + "%'";
                }
            }
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
            int pageCount = 0;//总页数
            int recordCount = retVal.size();//总记录数
            if (recordCount % size == 0) {
                pageCount = recordCount / size;
            } else {
                pageCount = recordCount / size + 1;
            }
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            if (retVal.size() > 0) {
                for (int j = 0; j < retVal.size(); j++) {
                    Map map = new HashMap();
                    map.put("areaId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_id"));
                    map.put("areaName", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_name"));
                    map.put("areaCreatetime", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_createtime"));
                    map.put("areaRemark", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_remark"));
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

    //根据Id查询区域
    @Override
    public Msg findByAreaId(Long areaId) {
        Msg msg = new Msg(false, "查询失败!");
        try {
            Area area = areaRepository.findByAreaId(areaId);
            msg.setObj(area);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

    //查询所有区域
    @Override
    public Msg findAllArea() {
        Msg msg = new Msg(false, "查询失败!");
        try {
            List<Area> areaList = (List<Area>) areaRepository.findAll();
            if (areaList == null) {
                msg.setMsg("区域不存在!");
                return msg;
            }
            msg.setObj(areaList);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

    //查询当前用户所拥有的区域
    @Override
    public Msg findAllAreaByUserName() {
        Msg msg = new Msg(false, "查询失败!");
        try {
            String userName = GetUuapUser.GetUser();
            if(userName==null){
                msg.setMsg("用户不存在");
                return msg;
            }
            List<UserArea> userAreaList = userAreaRepository.findByUserName(userName);
            if (userAreaList == null) {
                msg.setMsg("区域不存在!");
                return msg;
            }
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (int i = 0; i < userAreaList.size(); i++) {
                Area area = areaRepository.findByAreaId(userAreaList.get(i).getAreaId());
                Map map = new HashMap();

                map.put("areaName", area.getAreaName());
                map.put("id", userAreaList.get(i).getAreaId());
                mapList.add(map);
            }
            msg.setObj(mapList);
            msg.setSuccess(true);
            msg.setMsg("查询成功!");
        } catch (Exception e) {
            msg.setMsg("查询失败" + e);
        }
        return msg;
    }

}
