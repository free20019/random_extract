package com.twkf.service;

import com.twkf.dao.CommonDao;
import com.twkf.entity.BackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2020/10/26 - 19:20
 */
@Service
@Slf4j
public class CommonService {

    @Autowired
    CommonDao commonDao;

    private Map<String,Object> getParameterToMap(HttpServletRequest request, String... parameters){
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        for(String parameter : parameters){
            map.put(parameter,request.getParameter(parameter)==null?"":request.getParameter(parameter));
        }
        return map;
    }

    public BackMessage<Object> login(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"user_name","user_pwd");
        try{
            List<Map<String, Object>> list = commonDao.login(map);
            if(list.size() == 1){
                request.getSession().setAttribute("user_name", list.get(0).get("user_name"));
                request.getSession().setAttribute("user_id", list.get(0).get("user_id"));
                request.getSession().setMaxInactiveInterval(-1);
                return new BackMessage<Object>(200, "成功", list);
            }else if(list.size() > 1){
                return new BackMessage<Object>(400, "用户名多个", list);
            }else {
                return new BackMessage<Object>(400, "失败", null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> findUser(HttpServletRequest request) {
        try{
            List<Map<String, Object>> list = commonDao.findUser();
            return new BackMessage<Object>(200, "成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> findExtractType(HttpServletRequest request) {
        try{
            List<Map<String, Object>> list = commonDao.findRepairType();
            return new BackMessage<Object>(200, "成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> findCompany(HttpServletRequest request) {
        try{
            List<Map<String, Object>> list = commonDao.findCompany();
            return new BackMessage<Object>(200, "成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> findFileNumber(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"company_name","type_id");
        map.put("type_id",2);
        try{
            List<Map<String, Object>> list = commonDao.findFileNumber(map);
            return new BackMessage<Object>(200, "成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public synchronized BackMessage<Object> extract(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"company_name","user_id");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("type_id",2);
        try{
            List<Map<String, Object>> list = commonDao.findExtractFileNumber(map);
            if(list.size()==0){
                return new BackMessage<Object>(400,"失败", "该单位已无可抽取的无档案号");
            }
            Random random = new Random();
            int index = random.nextInt(list.size());
            map.put("file_id",list.get(index).get("id"));
            map.put("db_time",sdf.format(new Date()));
            Integer count = commonDao.insertExtractRecord(map);
            if(count == 1){
                return new BackMessage<Object>(200, "成功", list.get(index));
            }else{
                return new BackMessage<Object>(400,"失败", "入库失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> insertExtractRecord(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"file_id","user_id");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("type_id",2);
        try{
            map.put("db_time",sdf.format(new Date()));
            Integer count = commonDao.insertExtractRecord(map);
            if(count == 1){
                return new BackMessage<Object>(200, "成功", "入库成功");
            }else{
                return new BackMessage<Object>(400,"失败", "入库失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }


    public BackMessage<Object> deleteExtractRecord(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"record_id");
        try{
            Integer count = commonDao.deleteExtractRecord(map);
            if(count >= 1){
                return new BackMessage<Object>(200, "成功", "成功");
            }else{
                return new BackMessage<Object>(400,"失败", "失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> findExtractRecord(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"company_name","user_id","file_number","stime","etime","type_id");
        map.put("type_id",2);
        try{
            List<Map<String, Object>> list = commonDao.findExtractRecord(map);
            return new BackMessage<Object>(200, "成功", list);
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }

    public BackMessage<Object> insertPointInfo(HttpServletRequest request) {
        Map<String, Object> map = getParameterToMap(request,"name","px","py");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            map.put("db_time",sdf.format(new Date()));
            Integer count = commonDao.insertPointInfo(map);
            if(count == 1){
                return new BackMessage<Object>(200, "成功", "入库成功");
            }else{
                return new BackMessage<Object>(400,"失败", "入库失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BackMessage<Object>(400,"错误", e.getMessage());
        }
    }
}


