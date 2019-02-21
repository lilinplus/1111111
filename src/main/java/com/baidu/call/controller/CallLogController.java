package com.baidu.call.controller;

import com.baidu.call.service.CallLogService;
import com.baidu.call.utils.JsonUtils;
import com.baidu.call.utils.page.dtgrid.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "录音信息相关接口")
public class CallLogController {

    @Autowired
    private CallLogService callLogService;

    /**
     * 查询当前用户录音信息
     * @param pager
     * @param response
     */
    @ApiOperation(value = "查询当前用户录音信息", notes = "查询当前用户录音信息")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectCallLog", method = RequestMethod.GET)
    public void queryCallLog(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryCallLog(pager),response);
    }

    /**
     * 查询用户录音信息
     * @param pager
     * @param userName
     * @param response
     */
    @ApiOperation(value = "查询用户录音信息", notes = "查询用户录音信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "pager", name = "pager", dataType = "Pager"),
            @ApiImplicitParam(value = "userName", name = "userName", dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/call/selectCallLog{userName}", method = RequestMethod.GET)
    public void queryUserCallLog(Pager pager,@PathVariable String userName, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryUserCallLog(pager,userName),response);
    }

    /**
     * 查询当前用户负责的区域
     * @param response
     */
    @ApiOperation(value = "查询用户负责的区域", notes = "查询用户负责的区域")
    @RequestMapping(value = "/call/selectUserArea", method = RequestMethod.GET)
    public void queryUserArea(HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryUserArea(),response);
    }

    /**
     * 查询该区域下所有用户
     * @param areaName
     * @param response
     */
    @ApiOperation(value = "查询该区域下所有用户", notes = "查询该区域下所有用户")
    @ApiImplicitParam(name = "areaName", value = "areaName", dataType = "String",paramType = "path")
    @RequestMapping(value = "/call/selectAreaMember{areaName}", method = RequestMethod.GET)
    public void queryAreaMember(@PathVariable String areaName, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryAreaMember(areaName),response);
    }

    /**
     * 查询当前用户负责的组
     * @param response
     */
    @ApiOperation(value = "查询当前用户负责的组", notes = "查询当前用户负责的组")
    @RequestMapping(value = "/call/selectUserGroup", method = RequestMethod.GET)
    public void queryUserGroup(HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryUserGroup(),response);
    }

    /**
     * 查询分组成员
     * @param groupName
     * @param response
     */
    @ApiOperation(value = "查询分组成员", notes = "查询分组成员")
    @ApiImplicitParam(name = "groupName", value = "groupName", dataType = "String",paramType = "path")
    @RequestMapping(value = "/call/selectGroupMember{groupName}", method = RequestMethod.GET)
    public void queryGroupMember(@PathVariable String groupName, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryGroupMember(groupName),response);
    }

}
