package com.xy.Util;

import com.xy.onlineteam.common.utility.MD5Util;
import com.xy.onlineteam.common.utility.SignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: TANSHUFANG
 * @Description:组装请求头
 * @CreateDate: 2018/9/11 11:35
 */
public class RequestHeaderUtil {
    private Map<String, String> reqHeaders;

    public Map<String, String> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeadersStr, String signKey, String signByBody) {
        String[] strArr = reqHeadersStr.split(",");
        Map<String, String> reqHeadersTemp = new HashMap<String, String>();
        for (String strTemp : strArr) {
            String[] strArrTemp = strTemp.split("=");
            reqHeadersTemp.put(strArrTemp[0], strArrTemp[1]);
        }
        reqHeadersTemp.put("timestamp", Long.toString(System.currentTimeMillis()));
        String sign = "123456";
        if (!signByBody.isEmpty()) {
            TreeMap<String, String> treeMap = new TreeMap<String, String>();
            treeMap.putAll(reqHeadersTemp);
            treeMap.putAll(SignBody(signByBody));
            String keyValues = SignUtil.treeMapToString(treeMap, signKey);
            sign = MD5Util.md5Crypt(keyValues);
        }
        reqHeadersTemp.put("sign", sign);
        this.reqHeaders = reqHeadersTemp;
    }

    public Map<String, String> SignBody(String str) {
        Map<String, String> map = new HashMap<String, String>();
        String[] strArr = str.split(",");
        for (String strTemp : strArr) {
            String[] strArrTemp = strTemp.split("=");
            map.put(strArrTemp[0], strArrTemp[1]);
        }
        return map;
    }
}
