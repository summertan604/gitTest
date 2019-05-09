package com.xy.Util;

import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.List;
import java.util.Map;

/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2018/9/10 16:00
 */
public class ExcuteInsertUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcuteInsertUtil.class);
    public static void main(String[] args){
        try{
            ExcelUtil excelUtil=new ExcelUtil("test","test");
            List<Map<String,String>> listMap=excelUtil.readExcelData();
            int sum=InsertSqlUtil.executeInsert(listMap);
            logger.info("插入数据"+sum+"条");
        }catch (Exception e){
            logger.info(e.getMessage());
        }

    }


}
