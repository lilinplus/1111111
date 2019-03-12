package com.baidu.call.controller;

import com.baidu.call.model.PhoneUser;
import com.baidu.call.service.PhoneUserService;
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
@Api(tags = "用户使用话机时间段相关接口")
public class PhoneUserController {

    @Autowired
    private PhoneUserService phoneUserService;

    /**
     * 添加用户使用话机时间段记录
     * @param phoneUser
     * @param response
     */
    @ApiOperation(value = "添加记录", notes = "添加记录")
    @ApiImplicitParam(value = "phoneUser", name = "phoneUser", dataType = "PhoneUser")
    @RequestMapping(value = "/call/addPhoneUser", method = RequestMethod.POST)
    public void addPhoneUser(PhoneUser phoneUser, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(phoneUserService.addPhoneUser(phoneUser), response);
    }

    /**
     * 删除用户使用话机时间段记录
     * @param phoneUserId
     * @param response
     */
    @ApiOperation(value = "删除记录", notes = "删除记录")
    @ApiImplicitParam(value = "phoneUserId", name = "phoneUserId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deletePhoneUser/{phoneUserId}", method = RequestMethod.DELETE)
    public void deletePhoneUserId(@PathVariable Long phoneUserId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneUserService.deletePhoneUser(phoneUserId),response);
    }

    /**
     * 修改用户使用话机时间段记录
     * @param phoneUserId
     * @param phoneUser
     * @param response
     */
    @ApiOperation(value = "修改记录", notes = "修改记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "phoneUserId", name = "phoneUserId", dataType = "Long",paramType = "path"),
            @ApiImplicitParam(value = "phoneUser", name = "phoneUser", dataType = "PhoneUser")
    })
    @RequestMapping(value = "/call/updatePhoneUser/{phoneUserId}", method = RequestMethod.PUT)
    public void updatePhoneUser(@PathVariable Long phoneUserId, PhoneUser phoneUser, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneUserService.updatePhoneUser(phoneUserId,phoneUser),response);
    }

    /**
     * 查询用户使用话机时间段记录
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询记录", notes = "分页查询记录")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectPhoneUser", method = RequestMethod.GET)
    public void queryPhoneUser(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneUserService.queryPhoneUser(pager),response);
    }

    /**
     * 根据id查询用户使用话机时间段记录
     * @param phoneUserId
     * @param response
     */
    @ApiOperation(value = "根据id查询用户使用话机时间段记录", notes = "根据id查询用户使用话机时间段记录")
    @ApiImplicitParam(name = "phoneUserId", value = "phoneUserId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/findByPhoneUserId/{phoneUserId}", method = RequestMethod.GET)
    public void findByPhoneUserId(@PathVariable Long phoneUserId,HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneUserService.findByPhoneUserId(phoneUserId),response);
    }

}
