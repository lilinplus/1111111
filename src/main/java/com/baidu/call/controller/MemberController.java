package com.baidu.call.controller;

import com.baidu.call.model.Member;
import com.baidu.call.service.MemberService;
import com.baidu.call.utils.JsonUtils;
import com.baidu.call.utils.page.dtgrid.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "分组成员管理相关接口")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 添加分组成员
     * @param member
     * @param response
     */
    @ApiOperation(value = "添加分组成员", notes = "添加分组成员")
    @ApiImplicitParam(value = "member", name = "member", dataType = "Member")
    @RequestMapping(value = "/call/addmember", method = RequestMethod.POST)
    public void addMember(Member member, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(memberService.addMember(member), response);
    }

    /**
     * 删除分组成员
     * @param memberId
     * @param response
     */
    @ApiOperation(value = "删除分组成员", notes = "删除分组成员")
    @ApiImplicitParam(value = "memberId", name = "memberId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deletemember{memberId}", method = RequestMethod.DELETE)
    public void deleteMember(@PathVariable Long memberId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(memberService.deleteMember(memberId),response);
    }

    /**
     * 查询分组成员
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询分组成员", notes = "分页查询分组成员")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectmember", method = RequestMethod.GET)
    public void queryMember(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(memberService.queryMember(pager),response);
    }

}
