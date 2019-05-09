package com.xy.V2PubClass;


import com.xy.utility.MSignaturer;

import java.io.Serializable;
import java.util.Map;

public class V2PublicReqBody implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, String> requestBodyMap;

    public Map<String, String> getRequestBodyMap() {
        return requestBodyMap;
    }

    public void setRequestBodyMap(Map<String, String> requestBodyMap) {
        requestBodyMap.put("x", MSignaturer.sha256Encode("863274031048728"));// SHA256(SIM卡ICCID)
        requestBodyMap.put("p",MSignaturer.sha256Encode("356113091800939"));// SHA256(手机IMEI/MEID) 898603142502921
        requestBodyMap.put("ai", MSignaturer.sha256Encode("fe68b42b"));// SHA256(ANDROID_ID)
        requestBodyMap.put("si", "");// 请求签名
        requestBodyMap.put("net", "WIFI");// 手机网络状态：取值2G 3G 4G 5G WIFI
        this.requestBodyMap = requestBodyMap;
    }
}
