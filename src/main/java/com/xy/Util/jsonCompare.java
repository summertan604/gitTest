package com.xy.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2018/12/21 11:48
 */
public class jsonCompare {
    String tag="0";
    String inTest="测试存在，断言不存在的节点有：";
    String inFormal="断言存在，测试不存在的节点有：";
    String differenceValue="";

    @SuppressWarnings("unchecked")
    public  Map<String,String> compareJson(JSONObject json1, JSONObject json2, String key) {
//        JSONObject longJson= json1.size()>=json2.size()?json1:json2;
//        JSONObject sortJson= longJson==json1?json2:json1;
        Iterator<String> i = json1.keySet().iterator();
        while (i.hasNext()) {
            key = i.next();
            if(json2.containsKey(key)){
                compareJson(json1.get(key), json2.get(key), key);
            }else{
                compareJson("","",key);
            }

        }
        Map<String,String> result=new HashMap<>();
        result.put("tag",tag);
        result.put("inTest",inTest);
        result.put("inFormal",inFormal);
        result.put("differenceValue",differenceValue);
        return result;
    }

    public  void compareJson(Object json1, Object json2, String key) {
        if (json1 instanceof JSONObject) {
//            System.out.println("this JSONObject----" + key);
            compareJson((JSONObject) json1, (JSONObject) json2, key);
        } else if (json1 instanceof JSONArray) {
//            System.out.println("this JSONArray----" + key);
            compareJson((JSONArray) json1, (JSONArray) json2, key);
        } else if (json1 instanceof String) {
//            System.out.println("this String----" + key);
//            compareJson((String) json1, (String) json2, key);
            try {
                String json1ToStr = json1.toString();
                String json2ToStr = json2==null?"null":json2.toString();
                compareJson(json1ToStr, json2ToStr, key);
            } catch (Exception e) {
                System.out.println("转换发生异常 key:" + key);
                e.printStackTrace();
            }

        } else {
//            System.out.println("this other----" + key);
            compareJson(json1.toString(), json2.toString(), key);
        }
    }

    public  void compareJson(String str1, String str2, String key) {
        if(str1==str2 && str1.isEmpty()){
            tag="1";
            inFormal=inFormal+key+",";
            System.err.println("不一致：key:" + key + "  在json2中不存在");
        }
        if (!str1.equals(str2)) {
            tag="1";
            differenceValue=differenceValue+key+",";
            System.err.println("不一致key:" + key + ",json1:" + str1 + ",json2:" + str2);
        } else {
            System.out.println("一致：key:" + key + ",json1:" + str1 + ",json2:" + str2);
        }
    }

    public  void compareJson(JSONArray json1, JSONArray json2, String key) {
        if (json1 != null && json2 != null) {
            Iterator i1 = json1.iterator();
            Iterator i2 = json2.iterator();
            while (i1.hasNext()) {
                compareJson(i1.next(), i2.next(), key);
            }
        } else {
            if (json1 == null && json2 == null) {
                System.err.println("不一致：key:" + key + "  在json1和json2中均不存在");
            } else if (json1 == null) {
                System.err.println("不一致：key:" + key + "  在json1中不存在");
            } else if (json2 == null) {
                System.err.println("不一致：key:" + key + "  在json2中不存在");
            } else {
                System.err.println("不一致：key:" + key + "  未知原因");
            }

        }
    }

    private final static String st1 = "{\"body\":{\"pubInfoList\":[{\"menuInfo\":{\"menuList\":[{\"menuCode01\":\"01\",\"menuName\":\"乐视企业2\",\"menuType\":\"CALL\"},{\"menuCode\":\"02\",\"menuName\":\"乐视企业2\",\"menuType\":\"CALL\"},{\"menuCode\":\"03\",\"menuName\":\"乐视企业3\",\"menuType\":\"CALL\"}],\"menuModel\":\"1\",\"menuVersion\":\"20190329025058\",\"pubId\":1139637624,\"rid\":7498631},\"pubInfo\":{\"corpLevel\":0,\"pubName\":\"好欢螺\",\"pubType\":\"电子商务-电商B2C;\",\"pubTypeCode\":\"003003\",\"pubVersion\":\"20190328132717\",\"rid\":7498631,\"status\":1,\"versionCode\":\"20190328132717\"},\"pubNumList\":[{\"areaCode\":\"CN;\",\"f\":\"1\",\"identType\":4,\"num\":\"60499105401\",\"pubId\":1139637624,\"rid\":7498631,\"sign\":\"9326113dfd6d87626a12da1da461640ec53febc7cf9e5cf8aaca57c4ec4f4268\",\"status\":1,\"subVersion\":20190330152614,\"type\":1}]}],\"signStr\":\"tsf;\"},\"desc\":\"OK_WITH_VALUE\",\"status\":2,\"status02\":2}";
    private final static String st2 = "{\"body\":{\"pubInfoList\":[{\"menuInfo\":{\"menuList\":[{\"menuCode\":\"0101\",\"menuName\":\"乐视企业1\",\"menuType\":\"CALL\"},{\"menuCode\":\"02\",\"menuName\":\"乐视企业2\",\"menuType\":\"CALL\"},{\"menuCode\":\"03\",\"menuName\":\"乐视企业3\",\"menuType\":\"CALL\"}],\"menuModel\":\"1\",\"menuVersion\":\"20190329025058\",\"pubId\":1139637624,\"rid\":7498631},\"pubInfo\":{\"corpLevel\":0,\"pubName\":\"好欢螺\",\"pubType\":\"电子商务-电商B2C;\",\"pubTypeCode\":\"003003\",\"pubVersion\":\"20190328132717\",\"rid\":7498631,\"status\":1,\"versionCode\":\"20190328132717\"},\"pubNumList\":[{\"areaCode\":\"CN;\",\"f\":\"1\",\"identType\":4,\"num\":\"60499105401\",\"pubId\":1139637624,\"rid\":7498631,\"sign\":\"9326113dfd6d87626a12da1da461640ec53febc7cf9e5cf8aaca57c4ec4f4268\",\"status\":1,\"subVersion\":20190330152614,\"type\":1}]}],\"signStr\":\"tsf;\"},\"desc\":\"OK_WITH_VALUE\",\"status\":2}";

    public  void main(String[] args) {
//        System.out.println(st1);
        JSONObject jsonObject1 = JSONObject.parseObject(st1);
        JSONObject jsonObject2 = JSONObject.parseObject(st2);
        compareJson(jsonObject1, jsonObject2, null);
    }


}

