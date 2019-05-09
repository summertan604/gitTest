package com.xy.Util;

import java.util.*;

/**
 * @Author: TANSHUFANG
 * @Description:组装select语句
 * @CreateDate: 2018/9/10 14:38
 */
public class SelectSqlUtil {
    private String sql;
    public String getSql() {
        return sql;
    }
    public void setSql(String condition) {
        Map<String,String> conditionMap=new HashMap<String,String>();
        String[] strArr=condition.split(",");
        for(String strTemp:strArr){
            String[] strArrTemp=strTemp.split("=");
            conditionMap.put(strArrTemp[0],strArrTemp[1]);
        }
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("SELECT * FROM API_TESTCASE_TABLE WHERE ");
//        stringBuffer.append("SELECT * FROM testng_table WHERE ");
        Boolean firstRound=true;
        for (String mapKey:conditionMap.keySet()){
            if(firstRound){
                stringBuffer.append(mapKey+"='"+conditionMap.get(mapKey)+"'");
                firstRound=false;
            }else{
                stringBuffer.append(" AND "+mapKey+"='"+conditionMap.get(mapKey)+"'");
            }
        }
        this.sql = stringBuffer.toString();
    }
}
