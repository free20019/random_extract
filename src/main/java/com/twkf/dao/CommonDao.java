package com.twkf.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.PublicImpl;

import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2020/10/26 - 19:21
 */
public interface CommonDao {

    @Select("select * from extract_user where user_name=#{user_name} and user_pwd=#{user_pwd}")
    List<Map<String, Object>> login(Map<String, Object> map);

    @Select("select distinct user_name,id,real_name from extract_user")
    List<Map<String, Object>> findUser();

    @Select("select distinct type_id, type_name from extract_type")
    List<Map<String, Object>> findRepairType();

    @Select("select distinct company_name from extract_company_file where is_deleted = 0")
    List<Map<String, Object>> findCompany();

    @Select("select distinct id, file_number from extract_company_file" +
            " where is_deleted = 0 and company_name = #{company_name} and type_id = #{type_id}")
    List<Map<String, Object>> findFileNumber(Map<String, Object> map);

    @SelectProvider(type =ExtractRecord.class, method = "findExtractRecord")
    List<Map<String, Object>> findExtractRecord(Map<String, Object> map);

    @Insert("insert into extract_record (file_id, user_id, db_time) values (#{file_id}, #{user_id}, #{db_time})")
    Integer insertExtractRecord(Map<String, Object> map);

    @Update("update extract_record set is_deleted = 1 where record_id = #{record_id}")
    Integer deleteExtractRecord(Map<String, Object> map);

    @Select("select distinct id, file_number from extract_company_file f" +
            " left join extract_record r on f.id = r.file_id and r.user_id = #{user_id} and r.is_deleted = 0" +
            " where f.is_deleted = 0 and f.company_name = #{company_name} and f.type_id = #{type_id} and r.file_id is null")
    List<Map<String, Object>> findExtractFileNumber(Map<String, Object> map);

    @Insert("insert into a_caidian (name, px, py, db_time) values (#{name}, #{px}, #{py}, #{db_time})")
    Integer insertPointInfo(Map<String, Object> map);

    class ExtractRecord{
        public String findExtractRecord(Map<String, Object> map){
            String company_name = String.valueOf(map.get("company_name"));
            String user_id = String.valueOf(map.get("user_id"));
            String file_number = String.valueOf(map.get("file_number"));
            String stime = String.valueOf(map.get("stime"));
            String etime = String.valueOf(map.get("etime"));
            String type_id = String.valueOf(map.get("type_id"));
            String tj ="";
            if(isNotNull(company_name)){
                tj += " and b.company_name = '"+company_name+"'";
            }
            if(isNotNull(user_id)){
                tj += " and a.user_id = '"+user_id+"'";
            }
            if(isNotNull(file_number)){
                tj += " and b.file_number like '%"+file_number+"%'";
            }
            if(isNotNull(stime)){
                tj += " and a.db_time >= '"+stime+" 00:00:00'";
            }
            if(isNotNull(etime)){
                tj += " and a.db_time <= '"+etime+" 23:59:59'";
            }
            if(isNotNull(type_id)){
                tj += " and b.type_id = '"+type_id+"'";
            }

            String sql ="select a.*, b.company_name,b.file_number,b.type_id,b.type_name, c.user_name,c.real_name from extract_record a" +
                    " left join (select b.*,d.type_name from extract_company_file b,extract_type d where b.type_id = d.type_id) b on b.id = a.file_id" +
                    " left join extract_user c on c.user_id = a.user_id" +
                    " where a.is_deleted = 0 and b.is_deleted = 0";
            sql += tj;
            sql += " order by a.db_time desc";
            return sql;
        }

        public Boolean isNotNull(String str){
            if(str!=null&&!str.isEmpty()&&!"null".equals(str) &&str.length()>0){
                return true;
            }else{
                return false;
            }
        }
    }
}
