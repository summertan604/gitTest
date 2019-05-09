package com.xy.V2PubClass;


import com.xy.utility.MSignaturer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2019/1/9 11:59
 */
public class V2PublicReqBodyNew implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, Object> requestBodyMap;

    public Map<String, Object> getRequestBodyMap() {
        return requestBodyMap;
    }

    public void setRequestBodyMap(Map<String, Object> requestBodyMap) {
        requestBodyMap.put("x", MSignaturer.sha256Encode("863274031048728"));// SHA256(SIM卡ICCID)
        requestBodyMap.put("p",MSignaturer.sha256Encode("356113091800939"));// SHA256(手机IMEI/MEID) 898603142502921
        requestBodyMap.put("ai", MSignaturer.sha256Encode("fe68b42b"));// SHA256(ANDROID_ID)
        requestBodyMap.put("si", "");// 请求签名
        requestBodyMap.put("net", "WIFI");// 手机网络状态：取值2G 3G 4G 5G WIFI
        Map<String, String> newName = new HashMap<String, String>();
        newName.put("st","0");
        newName.put("nv","20181119101910");
        newName.put("ms","{xxxx}");
        Map<String, String> newName1 = new HashMap<String, String>();
        newName1.put("st","0");
        newName1.put("nv","20181119101910");
        newName1.put("ms","{xxxx}");
        Map<String, Object> jarNameMap = new HashMap<String, Object>();
        jarNameMap.put("engine_pkg.zip", newName);
        jarNameMap.put("inner_data_comm_MEIZU3_0361.zip", newName1);
        Map<String,Object> pkg=new HashMap<>();
        pkg.put("pkg",jarNameMap);
        requestBodyMap.put("extend", pkg);// 版本最近更新结果
        this.requestBodyMap = requestBodyMap;
    }
}