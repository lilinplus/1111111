package com.baidu.call.controller;

import com.baidu.call.model.Group;
import com.baidu.call.service.GroupService;
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
@Api(tags = "分组信息相关接口")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 添加分组信息
     * @param group
     * @param response
     */
    @ApiOperation(value = "添加分组", notes = "添加分组")
    @ApiImplicitParam(value = "group", name = "group", dataType = "Group")
    @RequestMapping(value = "/call/addgroup", method = RequestMethod.POST)
    public void addGroup(Group group, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(groupService.addGroup(group), response);
    }

    /**
     * 删除分组信息
     * @param groupId
     * @param response
     */
    @ApiOperation(value = "删除分组", notes = "删除分组")
    @ApiImplicitParam(value = "groupId", name = "groupId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deletegroup{groupId}", method = RequestMethod.DELETE)
    public void deleteGroupId(@PathVariable Long groupId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(groupService.deleteGroup(groupId),response);
    }

    /**
     * 修改分组信息
     * @param groupId
     * @param group
     * @param response
     */
    @ApiOperation(value = "修改分组", notes = "修改分组")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "groupId", name = "groupId", dataType = "Long",paramType = "path"),
            @ApiImplicitParam(value = "group", name = "group", dataType = "Group")
    })
    @RequestMapping(value = "/call/updategroup{groupId}", method = RequestMethod.PUT)
    public void updateGroup(@PathVariable Long groupId, Group group, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(groupService.updateGroup(groupId,group),response);
    }

    /**
     * 查询分组信息
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询分组", notes = "分页查询分组")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectgroup", method = RequestMethod.GET)
    public void queryGroup(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(groupService.queryGroup(pager),response);
    }

}
