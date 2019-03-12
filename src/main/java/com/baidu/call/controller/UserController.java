package com.baidu.call.controller;

import com.baidu.call.pojo.UserAreaVo;
import com.baidu.call.service.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加用户
     * @param userAreaVo
     * @param response
     */
    @ApiOperation(value = "添加用户", notes = "添加用户")
    @ApiImplicitParam(value = "userAreaVo", name = "userAreaVo", dataType = "UserAreaVo")
    @RequestMapping(value = "/call/addUser", method = RequestMethod.POST)
    public void addUser(UserAreaVo userAreaVo, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(userService.addUser(userAreaVo), response);
    }

    /**
     * 删除用户
     * @param userId
     * @param response
     */
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(value = "userId", name = "userId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deleteUser/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long userId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(userService.deleteUser(userId),response);
    }

    /**
     * 修改用户信息
     * @param userId
     * @param userAreaVo
     * @param response
     */
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userId", name = "userId", dataType = "Long",paramType = "path"),
            @ApiImplicitParam(value = "userAreaVo", name = "userAreaVo", dataType = "UserAreaVo")
    })
    @RequestMapping(value = "/call/updateUser/{userId}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable Long userId, UserAreaVo userAreaVo, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(userService.updateUser(userId,userAreaVo),response);
    }

    /**
     * 查询用户信息
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询用户", notes = "分页查询用户")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectUser", method = RequestMethod.GET)
    public void queryUser(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(userService.queryUser(pager),response);
    }

    /**
     * 根据Id查询用户
     * @param userId
     * @param response
     */
    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户")
    @ApiImplicitParam(name = "userId", value = "userId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/findByUserId/{userId}", method = RequestMethod.GET)
    public void findByUserId(@PathVariable Long userId,HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(userService.findByUserId(userId),response);
    }

//    /**
//     * 获取用户信息
//     * @param userName
//     * @param response
//     */
//    @ApiOperation(value = "获取用户信息",notes = "getUserInfoOne")
//    @RequestMapping(value = "/call/getUserInfoOne",method = RequestMethod.GET)
//    public void getUserInfoOne(String userName, HttpServletResponse response){
//        JsonUtils.writeJsonBySerializer(userService.getUserInfoOne(userName), response);
//    }
//
//    /**
//     * 根据域账户精确查询uic信息
//     *
//     * @param username
//     */
//    @ApiOperation(value = "查询uic用户信息", notes = "查询uic用户信息")
//    @ApiImplicitParam(value = "username", name = "username", dataType = "String",paramType = "path")
//    @RequestMapping(value = "/call/findUicUser/{username}", method = RequestMethod.GET)
//    public void findUserByUsernameUic(@PathVariable String username,HttpServletResponse response) {
//        JsonUtils.writeJsonBySerializer(userService.findUserByUsernameUic(username), response);
//    }

    /**
     * 获取当前登录用户的基本信息
     */
    @ApiOperation(value = "获取当前登录用户的基本信息", notes = "获取当前登录用户的基本信息")
    @RequestMapping(value = "/call/loginUserInfo", method = RequestMethod.GET)
    public void getLoginUserInfo(HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(userService.getLoginUserInfo(request), response);
    }

}
