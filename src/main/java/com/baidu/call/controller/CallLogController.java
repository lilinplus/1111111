package com.baidu.call.controller;

import com.baidu.call.model.CallLog;
import com.baidu.call.repository.CallLogRepository;
import com.baidu.call.service.CallLogService;
import com.baidu.call.utils.JsonUtils;
import com.baidu.call.utils.Msg;
import com.baidu.call.utils.page.dtgrid.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@Api(tags = "录音信息相关接口")
public class CallLogController {

    @Autowired
    private CallLogService callLogService;

    @Autowired
    private CallLogRepository callLogRepository;

    /**
     * 查询用户录音信息
     * @param pager
     * @param response
     */
    @ApiOperation(value = "查询用户录音信息", notes = "查询用户录音信息")
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
//    @ApiOperation(value = "查询用户录音信息", notes = "查询用户录音信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "pager", name = "pager", dataType = "Pager"),
//            @ApiImplicitParam(value = "userName", name = "userName", dataType = "String",paramType = "path")
//    })
//    @RequestMapping(value = "/call/selectCallLog/{userName}", method = RequestMethod.GET)
//    public void queryUserCallLog(Pager pager,@PathVariable String userName, HttpServletResponse response){
//        JsonUtils.writeJsonBySerializer(callLogService.queryUserCallLog(pager,userName),response);
//    }

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
    @RequestMapping(value = "/call/selectAreaMember/{areaName}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/call/selectGroupMember/{groupName}", method = RequestMethod.GET)
    public void queryGroupMember(@PathVariable String groupName, HttpServletResponse response){
        JsonUtils.writeJsonBySerializer(callLogService.queryGroupMember(groupName),response);
    }

    /**
     * 文件下载
     */
    @ResponseBody
    @ApiOperation(value = "附件下载", notes = "downloadFile")
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public Msg downloadFile(HttpServletResponse response,Long id) {
        Msg msg = new Msg();
        try {
            CallLog callLog=callLogRepository.findCallLogById(id);
            String filePathName=callLog.getRecordingFilename();
            String fileName = filePathName.split("/")[filePathName.split("/").length - 1].toString();
            File file = new File(filePathName);
            if (!file.exists()) {
                msg.setMsg("文件不存在");
                return msg;
            }
            response.reset();
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition","attachment;filename="+new String(fileName.getBytes("UTF-8"),"iso-8859-1"));

            InputStream inStream = new FileInputStream(filePathName);
            OutputStream os = response.getOutputStream();
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();
            inStream.close();
            msg.setMsg("下载成功");
            msg.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

}
