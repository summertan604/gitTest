package com.xy.V0PubClass;

import com.xy.onlineteam.common.utility.Signaturer;
import com.xy.utility.AppKeyDoList;
import com.xy.utility.MSignaturer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class V0ReqHeaders {
    private static final Logger log = LoggerFactory.getLogger(V0ReqHeaders.class);
    private static final Charset charset = Charset.forName("UTF-8");
    private static String DUOQU_SDK_APP_KEY = null;
    private static String DUOQU_SDK_RSAPRVKEY = null;
    public static HashMap<String, String> reqHeaders(String chanel,String imei) {
        Map<String, String> appKey = AppKeyDoList.getAppKey(chanel);
         DUOQU_SDK_APP_KEY = appKey.get("DUOQU_SDK_APP_KEY");
        DUOQU_SDK_RSAPRVKEY = appKey.get("DUOQU_SDK_RSAPRVKEY-1");

        HashMap<String, String> reqHeaders = new HashMap<String, String>();
        reqHeaders.put("app-key", appKey.get("DUOQU_SDK_APP_KEY"));// 分配的应用渠道的key
        String compress="1";
        reqHeaders.put("cmd", "990005");// 目前固定值990005
        reqHeaders.put("command", "1");// 1 非token
        reqHeaders.put("compress", compress);// 1是否加密
        if (compress.equals("1")) {
            reqHeaders.put("nz", 1 + "");
            reqHeaders.put("encrypt", 2 + "");// 固定为2
        }
        reqHeaders.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.0.0; M10 Build/NRD90M)");
        reqHeaders.put("sdkversion", "201509111430");
        reqHeaders.put("app-key-sign", getSign(reqHeaders.get("sdkversion"),DUOQU_SDK_APP_KEY,DUOQU_SDK_RSAPRVKEY));// 私钥签名
        reqHeaders.put("p", MSignaturer.sha256Encode(imei));// IMEI号//MSignaturer.sha256Encode("666677776666")
        reqHeaders.put("uid", MSignaturer.sha256Encode(imei));// IMEI号
        reqHeaders.put("sceneType", "1");// 0 代表弹窗 1 代表丰富气泡
        reqHeaders.put("ai", MSignaturer.sha256Encode(imei));// SHA256(ANDROID_ID)
        reqHeaders.put("ni", MSignaturer.sha256Encode(imei));// SHA256(ANDROID_ID)
        reqHeaders.put("xid", MSignaturer.sha256Encode(imei));// SHA256(ICCID)
        reqHeaders.put("uv", MSignaturer.sha256Encode(imei));
        reqHeaders.put("net", "wifi");
        reqHeaders.put("usid",imei);// 手机IMEI的明文
        return reqHeaders;
    }
    /**
     * 201510090900版本之前和版本之后，sign字段的获取方式不一样，适用于V0协议
     *
     * @param reqSdkversion
     * @return
     */
    public static String getSign(String reqSdkversion,String rsaprvKey,String appkey) {
        String sign = null;
        try {
            Date req_sdkversion = new SimpleDateFormat("yyyyMMddHHmm").parse(reqSdkversion);
            Date sdkversion = new SimpleDateFormat("yyyyMMddHHmm").parse("201510090900");
            if (req_sdkversion.before(sdkversion) || req_sdkversion.equals(sdkversion)) {
                log.debug("reqSdkversion≤201510090900");
                sign = Signaturer.sign(DUOQU_SDK_RSAPRVKEY, DUOQU_SDK_APP_KEY).replaceAll("\r|\n", "");
            } else if (req_sdkversion.after(sdkversion)) {
                log.debug("reqSdkversion＞201510090900");
                sign = Signaturer.sign(DUOQU_SDK_RSAPRVKEY, DUOQU_SDK_APP_KEY, Signaturer.SHA256_WITH_RSA)
                        .replaceAll("[\r\n]", "");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sign;
    }
}
