package com.xy.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2018/9/10 15:26
 */
public class InsertSqlUtil {


    public static int executeInsert(List<Map<String,String>> listMap) throws  ClassNotFoundException, SQLException {
        Connection con = new MySqlUtil().connectMysql();
        int i = 0;
        //设置批量处理的数量
        int batchSize = 5000;
        PreparedStatement stmt = con.prepareStatement("insert into API_TESTCASE_TABLE "
                +"(CASENAME,EXECUTESORT,PROJECTNAME,APINAME,ENVIRONMENT,REQUESTBODY,REQUESTHEADER,RESPONSEBODY,ASSERT,CREATTIME,CHAN,IMEI) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?)");
        // 关闭事务自动提交 ,这一行必须加上
        con.setAutoCommit(false);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int j = 0; j < listMap.size(); j++){
            ++i;
            Map<String,String> mapTemp=listMap.get(j);
            stmt.setString(1, mapTemp.get("CASENAME"));
            stmt.setString(2, mapTemp.get("EXECUTESORT"));
            stmt.setString(3, mapTemp.get("PROJECTNAME"));
            stmt.setString(4, mapTemp.get("APINAME"));
            stmt.setString(5, mapTemp.get("ENVIRONMENT"));
            stmt.setString(6, mapTemp.get("REQUESTBODY"));
            stmt.setString(7, mapTemp.get("REQUESTHEADER"));
            stmt.setString(8, mapTemp.get("RESPONSEBODY"));
            stmt.setString(9, mapTemp.get("ASSERT"));
            stmt.setString(10, sdf.format(new Date()));
            stmt.setString(11, mapTemp.get("CHAN"));
            stmt.setString(12, mapTemp.get("IMEI"));
            stmt.addBatch();
            if ( i % batchSize == 0 ) {
                stmt.executeBatch();
                con.commit();
            }
        }
        if ( i % batchSize != 0 ) {
            stmt.executeBatch();
            con.commit();
        }
        return i;
    }
}
