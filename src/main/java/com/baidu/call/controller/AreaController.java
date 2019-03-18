package com.baidu.call.controller;

import com.baidu.call.model.Area;
import com.baidu.call.service.AreaService;
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
@Api(tags = "区域信息相关接口")
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 添加区域
     * @param area
     * @param response
     */
    @ApiOperation(value = "添加区域", notes = "添加区域")
    @ApiImplicitParam(value = "area", name = "area", dataType = "Area")
    @RequestMapping(value = "/call/addArea", method = RequestMethod.POST)
    public void addArea(Area area, HttpServletResponse response) {
        JsonUtils.writeJsonBySerializer(areaService.addArea(area), response);
    }

    /**
     * 删除区域
     * @param areaId
     * @param response
     */
    @ApiOperation(value = "删除区域", notes = "删除区域")
    @ApiImplicitParam(value = "areaId", name = "areaId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/deleteArea/{areaId}", method = RequestMethod.DELETE)
    public void deleteArea(@PathVariable Long areaId, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.deleteArea(areaId),response);
    }

    /**
     * 修改区域信息
     * @param areaId
     * @param area
     * @param response
     */
    @ApiOperation(value = "修改区域", notes = "修改区域")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "areaId", name = "areaId", dataType = "Long",paramType = "path"),
            @ApiImplicitParam(value = "area", name = "area", dataType = "Area")
    })
    @RequestMapping(value = "/call/updateArea/{areaId}", method = RequestMethod.PUT)
    public void updateArea(@PathVariable Long areaId,Area area,HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.updateArea(areaId,area),response);
    }

    /**
     * 查询区域信息
     * @param pager
     * @param response
     */
    @ApiOperation(value = "分页查询区域", notes = "分页查询区域")
    @ApiImplicitParam(name = "pager", value = "pager", dataType = "Pager")
    @RequestMapping(value = "/call/selectArea", method = RequestMethod.GET)
    public void queryArea(Pager pager, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.queryArea(pager),response);
    }

    /**
     * 根据Id查询区域
     * @param areaId
     * @param response
     */
    @ApiOperation(value = "根据Id查询区域", notes = "根据Id查询区域")
    @ApiImplicitParam(name = "areaId", value = "areaId", dataType = "Long",paramType = "path")
    @RequestMapping(value = "/call/findByAreaId/{areaId}", method = RequestMethod.GET)
    public void findByAreaId(@PathVariable Long areaId,HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.findByAreaId(areaId),response);
    }

    /**
     * 查询所有区域
     * @param response
     */
    @ApiOperation(value = "查询所有区域", notes = "查询所有区域")
    @RequestMapping(value = "/call/findAllArea", method = RequestMethod.GET)
    public void findAllArea(HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.findAllArea(),response);
    }

    /**
     * 查询当前用户所拥有的区域
     * @param response
     */
    @ApiOperation(value = "查询当前用户所拥有的区域", notes = "查询当前用户所拥有的区域")
    @RequestMapping(value = "/call/findAllAreaByUserName", method = RequestMethod.GET)
    public void findAllAreaByUserName(HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(areaService.findAllAreaByUserName(),response);
    }

}
