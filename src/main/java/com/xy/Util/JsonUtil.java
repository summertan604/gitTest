package com.xy.Util;


import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONString;

/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2018/9/11 15:19
 */
public class JsonUtil {


    //1 json解析方法

    public static String getValueByJPath(String result, String assertfield) {
        JSONObject jsonObject = new JSONObject(result);
        Integer jsonString=jsonObject.getInt(assertfield);
        return String.valueOf(jsonString);
    }


}
