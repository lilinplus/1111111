package com.baidu.call.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.call.model.Member;
import com.baidu.call.repository.MemberRepository;
import com.baidu.call.service.CommonService;
import com.baidu.call.service.MemberService;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.baidu.call.utils.PropertyFieldConvertor.propertyToField;

@Service
public class MemberServiceImpl implements MemberService {

    private Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private MemberRepository memberRepository;

    //为分组添加成员
    @Override
    public Msg addMember(Member member) {
        Msg msg = new Msg(false,"添加失败");
        try {
            Long memberGroupId=member.getMemberGroupId();
            String memberName=member.getMemberName();
            if(memberGroupId!=null && !"".equals(memberGroupId)){
                if(memberName!=null && !"".equals(memberName)){
                    List<Member> memberList=memberRepository.findByMemberGroupId(memberGroupId);
                    for(int i=0;i<memberList.size();i++){
                        String memberName1=memberList.get(i).getMemberName();
                        if(memberName1.equals(memberName)){
                            msg.setMsg("域用户在该分组中已存在");
                            return msg;
                        }
                    }
                    memberRepository.save(member);
                    msg.setSuccess(true);
                    msg.setMsg("添加成功");
                }else {
                    msg.setMsg("成员域用户不能为空");
                    return msg;
                }
            }else {
                msg.setMsg("分组不能为空");
                return msg;
            }
        }catch (Exception e){
            msg.setMsg("添加失败"+e);
        }
        return msg;
    }

    //删除分组成员
    @Override
    public Msg deleteMember(Long memberId) {
        Msg msg = new Msg(false,"删除失败");
        try {
            Member member=memberRepository.findByMemberId(memberId);
            if(member==null){
                msg.setMsg("信息不存在");
                return msg;
            }
            memberRepository.deleteById(memberId);
            msg.setSuccess(true);
            msg.setMsg("删除成功");
        }catch (Exception e){
            msg.setMsg("删除失败"+e);
        }
        return msg;
    }

//    @Override
//    public Msg updateMember(Long memberId, Member member) {
//        Msg msg = new Msg(false,"更新成功");
//        try {
//
//        }catch (Exception e){
//            msg.setMsg("更新失败"+e);
//        }
//        return null;
//    }

    @Override
    public Pager queryMember(Pager pager) {
        Integer page = pager.getNowPage();
        Integer size = pager.getPageSize();
        Map<String,Object> parameters = pager.getParameters();
        List<com.baidu.call.utils.page.dtgrid.Sort> orderBy = pager.getAdvanceQuerySorts();
        try {
            String sql = "select cm.member_id memberId,cg.group_name groupName,cm.member_name memberName from call_member cm inner join call_group cg on cm.member_group_id=cg.group_id where 1=1";
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
