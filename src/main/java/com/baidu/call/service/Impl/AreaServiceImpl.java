package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.Area;
import com.baidu.call.repository.AreaRepository;
import com.baidu.call.service.AreaService;
import com.baidu.call.service.CommonService;
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
public class AreaServiceImpl implements AreaService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private AreaRepository areaRepository;

    //添加区域
    @Override
    public Msg addArea(Area area) {
        Msg msg = new Msg(false, "添加失败");
        try {
            if(area.getAreaName()!=null){
                Area area1=areaRepository.findByAreaName(area.getAreaName());
                if(area1==null){
                    area.setAreaCreatetime(new Date().getTime());
                    areaRepository.save(area);
                    msg.setSuccess(true);
                    msg.setMsg("添加成功");
                }else {
                    msg.setMsg("区域名已存在");
                    return msg;
                }
            }else {
                msg.setMsg("区域名不能为空");
                return msg;
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    //删除区域
    @Override
    public Msg deleteArea(Long areaId) {
        Msg msg = new Msg(false, "删除失败");
        try {
            Area area=areaRepository.findByAreaId(areaId);
            if(area==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            areaRepository.deleteById(areaId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

    //修改区域信息
    @Override
    public Msg updateArea(Long areaId, Area area) {
        Msg msg = new Msg(false, "修改失败");
        try {
            List list = ValidatorUtil.validateList(area);
            if(list != null && list.size() > 0){
                msg.setMsg(list.get(0).toString());
                return msg;
            }
            if(!areaId.toString().equals(areaRepository.findByAreaId(areaId).getAreaId().toString())){
                msg.setMsg("信息错误，请检查更新的信息");
                return msg;
            }
            if(areaRepository.findByAreaId(areaId)==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            if(area.getAreaName()!=null && !"".equals(area.getAreaName())){
                Area area1=areaRepository.findByAreaName(area.getAreaName());
                if(area1==null || area1.getAreaId()==areaId){
                    areaRepository.save(area);
                    msg.setSuccess(true);
                    msg.setMsg("修改成功");
                }else {
                    msg.setMsg("区域名已存在");
                }
            }else {
                msg.setMsg("区域名不能为空");
            }
        }catch (Exception e){
            msg.setMsg("修改失败"+e);
        }
        return msg;
    }

    //查询区域信息
    @Override
    public Pager queryArea(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select * from call_area";
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
                    map.put("areaId", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_id"));
                    map.put("areaName", JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_name"));
                    map.put("areaCreatetime",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_createtime"));
                    map.put("areaRemarks",JSONObject.parseObject(JSON.toJSONString(retVal.get(j))).get("area_remarks"));
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
