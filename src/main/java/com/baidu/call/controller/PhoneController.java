package com.baidu.call.controller;

import com.baidu.call.model.Phone;
import com.baidu.call.service.PhoneService;
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
@Api(tags = "话机相关接口")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    /**
     * 添加话机
     * @param phone
     * @param response
     */
    @ApiOperation(value = "添加话机", notes = "添加话机")
    @ApiImplicitParam(value = "phone", name = "phone", dataType = "Phone")
    @RequestMapping(value = "/call/addphone", method = RequestMethod.POST)
    public void addPhone(Phone phone, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(phoneService.addPhone(phone), response);
    }

    /**
     * 删除话机
     * @param phoneId
     * @param response
     */
    @ApiOperation(value = "删除话机", notes = "删除话机")
    @ApiImplicitParam(value = "phoneId", name = "phoneId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deletephone/{phoneId}", method = RequestMethod.DELETE)
    public void deletePhoneId(@PathVariable Long phoneId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneService.deletePhone(phoneId),response);
    }

    /**
     * 修改话机
     * @param phoneId
     * @param phone
     * @param response
     */
    @ApiOperation(value = "修改话机", notes = "修改话机")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "phoneId", name = "phoneId", dataType = "Long",paramType = "path"),
            @ApiImplicitParam(value = "phone", name = "phone", dataType = "Phone")
    })
    @RequestMapping(value = "/call/updatephone/{phoneId}", method = RequestMethod.PUT)
    public void updatePhone(@PathVariable Long phoneId, Phone phone, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneService.updatePhone(phoneId,phone),response);
    }

    /**
     * 查询话机
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询话机", notes = "分页查询话机")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectphone", method = RequestMethod.GET)
    public void queryPhone(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneService.queryPhone(pager),response);
    }

    /**
     * 根据Id查询分机号
     * @param phoneId
     * @param response
     */
    @ApiOperation(value = "根据Id查询分机号", notes = "根据Id查询分机号")
    @ApiImplicitParam(name = "phoneId", value = "phoneId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/findByPhoneId/{phoneId}", method = RequestMethod.GET)
    public void findByPhoneId(@PathVariable Long phoneId,HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(phoneService.findByPhoneId(phoneId),response);
    }

}
