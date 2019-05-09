package com.xy.Util;


import java.io.*;
import java.util.Properties;

/**
 * @Author: TANSHUFANG
 * @Description:常量定义工具类
eDate: 2018/9/10 14:25
 */
public class ConstDefineUtil {
    public Properties prop;

    public static final String MYSQL_USERNAME="root";
    public static final String MYSQL_PASSWORD="123456";

    public static final String PROPERTIES_CONDITION="CONDITION";
    public static final String PROPERTIES_URL="URL";
    public static final String PROPERTIES_REQUESTHEADER="REQUESTHEADER";
    public static final String PROPERTIES_APPSECREPT="APPSECREPT";
    public static final String PROPERTIES_APPKEY="APPKEY";
    public static final String PROPERTIES_SIGNBYBODY="SIGNBYBODY";
    public static final String PROPERTIES_XLSXPATH="XLSXPATH";

    /**
     * 构造函数读取properties文件
     */
    public ConstDefineUtil() {
        try{
            prop= new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+
                    "/src/main/java/com/xy/config/config.properties");
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis,"iso-8859-1"));
            prop.load(bf);


        }catch (FileNotFoundException e) {
            e.printStackTrace();

        }catch (IOException e) {
            e.printStackTrace();

        }
    }
}
