package com.xy.Util;

import java.sql.*;
import java.util.*;

/**
 * @Author: TANSHUFANG
 * @Description:连接mysql数据库
 * @CreateDate: 2018/9/10 14:35
 */
public class MySqlUtil  {

    public Connection connectMysql() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://192.168.101.213:3306/test?characterEncoding=UTF-8";
        System.out.println(url);
        //获取连接
        Connection conn = DriverManager.getConnection(url, ConstDefineUtil.MYSQL_USERNAME, ConstDefineUtil.MYSQL_PASSWORD);
        return conn;
    }
}
