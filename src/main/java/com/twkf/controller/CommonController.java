package com.twkf.controller;

import com.twkf.entity.BackMessage;
import com.twkf.helper.DownloadAct;
import com.twkf.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2020/10/26 - 19:18
 */

@Controller
@RequestMapping("/vehicleRepair")
//@CrossOrigin
@Slf4j
@Api(tags = "随机抽取")
public class CommonController {

    @Autowired
    CommonService commonService;

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_name" ,value = "用户名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "user_pwd" ,value = "密码",required = true,dataType = "String"),
    })
    public BackMessage<Object> login(HttpServletRequest request) {
        return commonService.login(request);
    }

    @GetMapping("/findUser")
    @ResponseBody
    @ApiOperation(value = "用户下拉框", hidden = true)
    @ApiImplicitParams({
    })
    public BackMessage<Object> findUser(HttpServletRequest request) {
        return commonService.findUser(request);
    }

    @GetMapping("/findExtractType")
    @ResponseBody
    @ApiOperation(value = "抽取类型下拉框", hidden = true)
    @ApiImplicitParams({
    })
    public BackMessage<Object> findExtractType(HttpServletRequest request) {
        return commonService.findExtractType(request);
    }

    @GetMapping("/findCompany")
    @ResponseBody
    @ApiOperation(value = "单位下拉框")
    @ApiImplicitParams({
    })
    public BackMessage<Object> findCompany(HttpServletRequest request) {
        return commonService.findCompany(request);
    }

    @GetMapping("/findFileNumber")
    @ResponseBody
    @ApiOperation(value = "档案号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_name" ,value = "单位",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "type_id" ,value = "抽取类型id",required = true,dataType = "Integer"),
    })
    public BackMessage<Object> findFileNumber(HttpServletRequest request) {
        return commonService.findFileNumber(request);
    }

    @GetMapping("/extract")
    @ResponseBody
    @ApiOperation(value = "抽取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_name" ,value = "单位",required = true,dataType = "String"),
            @ApiImplicitParam(name = "user_id" ,value = "用户ID",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "type_id" ,value = "抽取类型id",required = true,dataType = "Integer"),
    })
    public BackMessage<Object> extract(HttpServletRequest request) {
        return commonService.extract(request);
    }

    @GetMapping("/insertExtractRecord")
    @ResponseBody
    @ApiOperation(value = "抽取记录入库", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file_id" ,value = "抽取返回的id",required = true,dataType = "String"),
            @ApiImplicitParam(name = "user_id" ,value = "用户ID",required = true,dataType = "String"),
//            @ApiImplicitParam(name = "type_id" ,value = "抽取类型id",required = true,dataType = "Integer"),
    })
    public BackMessage<Object> insertExtractRecord(HttpServletRequest request) {
        return commonService.insertExtractRecord(request);
    }

    @GetMapping("/deleteExtractRecord")
    @ResponseBody
    @ApiOperation(value = "抽取记录删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "record_id" ,value = "抽取记录id",required = true,dataType = "String"),
    })
    public BackMessage<Object> deleteExtractRecord(HttpServletRequest request) {
        return commonService.deleteExtractRecord(request);
    }

    @GetMapping("/findExtractRecord")
    @ResponseBody
    @ApiOperation(value = "抽取记录查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_name" ,value = "单位",dataType = "String"),
            @ApiImplicitParam(name = "user_id" ,value = "用户ID",dataType = "String"),
            @ApiImplicitParam(name = "file_number" ,value = "案卷号",dataType = "String"),
            @ApiImplicitParam(name = "stime" ,value = "开始时间（例：2020-11-16）",dataType = "String"),
            @ApiImplicitParam(name = "etime" ,value = "开始时间（例：2020-11-16）",dataType = "String"),
//            @ApiImplicitParam(name = "type_id" ,value = "抽取类型id",required = true,dataType = "Integer"),
    })
    public BackMessage<Object> findExtractRecord(HttpServletRequest request) {
        return commonService.findExtractRecord(request);
    }

    @GetMapping("/findExtractRecordExcel")
    @ResponseBody
    @ApiOperation(value = "抽取记录导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "company_name" ,value = "单位",dataType = "String"),
            @ApiImplicitParam(name = "user_id" ,value = "用户ID",dataType = "String"),
            @ApiImplicitParam(name = "file_number" ,value = "案卷号",dataType = "String"),
            @ApiImplicitParam(name = "stime" ,value = "开始时间（例：2020-11-16）",dataType = "String"),
            @ApiImplicitParam(name = "etime" ,value = "开始时间（例：2020-11-16）",dataType = "String"),
//            @ApiImplicitParam(name = "type_id" ,value = "抽取类型id",required = true,dataType = "Integer"),
    })
    public BackMessage<Object> findExtractRecordExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String a[] = {"单位","档案号","类型","用户名","抽取时间"};//导出列明
        String b[] = {"company_name", "file_number","type_name", "user_name", "db_time"};//导出map中的key
        String gzb = "抽取记录";//导出sheet名和导出的文件名
        BackMessage<Object> msg = commonService.findExtractRecord(request);
        List<Map<String, Object>> list = (List<Map<String, Object>>) msg.getData();
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("order_number",i+1);
            }
        }
        DownloadAct.download(request,response,a,b,gzb,list);
        return null;
    }

    @GetMapping("/insertPointInfo")
    @ResponseBody
    @ApiOperation(value = "场站踩点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name" ,value = "点位信息",dataType = "String"),
            @ApiImplicitParam(name = "px" ,value = "经度",dataType = "String"),
            @ApiImplicitParam(name = "py" ,value = "纬度",dataType = "String"),
    })
    public BackMessage<Object> insertPointInfo(HttpServletRequest request){
        return commonService.insertPointInfo(request);
    }
}
