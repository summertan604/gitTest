package TestCase.InsertData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xy.V2PubClass.V2PublicReqBody;
import com.xy.V2PubClass.V2ReqHeaders;
import com.xy.V2PubClass.V2TokenTest;
import com.xy.onlineteam.common.utility.Base64;
import com.xy.onlineteam.common.utility.MD5Util;
import com.xy.onlineteam.common.utility.SignUtil;
import com.xy.onlineteam.sdkapi.util.TimeValidator;
import com.xy.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;

public class NewPub_Identify {
    private static Logger logger = LoggerFactory.getLogger(NewPub_Identify.class);
    private static Charset charset = Charset.forName("UTF-8");
    private V2ReqHeaders v2ReqHeaders = new V2ReqHeaders();
    private V2PublicReqBody v2PublicReqBody = new V2PublicReqBody();
    private HttpClientRequest request = new HttpClientRequest();
    private Map<String, String> requestHeaders;
    private Map<String, String> requestBodyMap;
    private Map<String, Object> requestBodyMap1;
    private Map<String, String> pubRequestBodyMap;
    private TreeMap<String, String> mRequestBodyMap;
    private String token = null;
    private byte[] aesKeyBytes = null;
    private byte[] aesIVBytes = null;
    private String DUOQU_SDK_APP_KEY;
    private String DUOQU_SDK_RSAPRVKEY;
    String chan="HUAWEI";
    String imei="256859695";
    String casename="可签名可区域号码_错误签名正确区域_返回区域对应企业";

    public Map<String, Object> setRequestBodyMap() {
        Map<String, Object> reqBodyMap=new HashMap<>();
        reqBodyMap.put("timemills", TimeValidator.getTimeMills(chan, MSignaturer.sha256Encode(imei)));// 短信接入码
        reqBodyMap.put("p",MSignaturer.sha256Encode(imei));
        //组装numParams
        Map<String,Object> numParamsMap=new HashMap<>();
        numParamsMap.put("requestType","2");
        numParamsMap.put("iccid","2");
        numParamsMap.put("imsi","2");
        numParamsMap.put("cnum","2");
        numParamsMap.put("mcc","2");
        numParamsMap.put("mnc","2");
        numParamsMap.put("lac","2");
        numParamsMap.put("ci","2");


        //组装numListMap
        Map<String,String> numListMap=new HashMap<>();
        numListMap.put("num","60499105401");
        numListMap.put("sign",EncryptUtil.string2SHA256StrJava("好欢螺"));
//        numListMap.put("sign","飞信65556");
        numListMap.put("versionCode","2010010101011");
        numListMap.put("menuVersion","20181226010134");
        numListMap.put("pubVersion","20190327084537");
        numListMap.put("subVersion","20190313115128");
        numListMap.put("areaCode","GD");
        numListMap.put("sg","tsf");
        numListMap.put("f","2");
        numListMap.put("ac","2");
        numListMap.put("rc","2");
        numListMap.put("dt","2");
        numListMap.put("tc","2");
        numListMap.put("ec","2");
        Map<String,String> numListMap2=new HashMap<>();
        numListMap2.put("num","60499105411");
        numListMap2.put("sign",EncryptUtil.string2SHA256StrJava("悲伤蛙1"));
        numListMap2.put("areaCode","CN");
        numListMap2.put("userAreaCode","0775");
        numListMap2.put("sg","tsf");
        numListMap2.put("versionCode","2010010101011");
        numListMap2.put("menuVersion","20190329123946");
        numListMap2.put("pubVersion","2010010101010");
        numListMap2.put("subVersion","20190329162818");
        numListMap2.put("f","2");
        numListMap2.put("ac","2");
        numListMap2.put("rc","2");
        numListMap2.put("dt","2");
        numListMap2.put("tc","2");
        numListMap2.put("ec","2");


        List<Map<String,String>> numList=new ArrayList<>();
        numList.add(numListMap);
//        numList.add(numListMap2);
//
        numParamsMap.put("numList",numList);

        List<Map<String,Object>> numParams=new ArrayList<>();
        numParams.add(numParamsMap);

        reqBodyMap.put("numParams", numParams);// 短信接入码






        return  reqBodyMap;
    }

    @BeforeClass
    public void beforeClass() {
        Map<String, String> appKey = AppKeyDoList.getAppKey(chan);
        DUOQU_SDK_APP_KEY = appKey.get("DUOQU_SDK_APP_KEY");
        DUOQU_SDK_RSAPRVKEY = appKey.get("DUOQU_SDK_RSAPRVKEY-2");

        String tkResponseBody = new V2TokenTest().getResponseBody(DUOQU_SDK_APP_KEY, "http://sdkapiv2.bizport.cn/token");
        JSONObject tkResponseBodyJson = JSON.parseObject(tkResponseBody);
        token = JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("token").toString();
        aesKeyBytes = com.xy.onlineteam.common.utility.Base64
                .decode(JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("aeskey").toString());
        aesIVBytes = Base64.decode(JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("iv").toString());

//        token = "KX09w4W2WuzG3kkCTghJJl8QQH99oAC9P88RL+GISTgf3yXV+A5jERb7tNb/QIvSqSFpCSMbwAOEpaJh60uYU8d6xwXunE76ZQoOaO6qi/o=";
//        aesKeyBytes = Base64.decode("ycsqlJnJYgRVM6ne/BNP6A==");
//        aesIVBytes = Base64.decode("EjRWeJCrze+pt8jW4/Ef/g==");

        // 接口URL 192.168.101.214:8398  120.27.150.234 http://120.27.150.234:8398/business/config/pull
//        request.setUrl("http://sdkapiv2.bizport.cn/business/pubinfo/identify");  //正式地址
        request.setUrl("http://192.168.101.214:8398/business/pubinfo/identify");
//
        logger.info("Url:" + request.getUrl());
    }

    /**
     * MAC地址接口
     */
    @Test
    public void testCase001_nz1_crypt3() throws SQLException, ClassNotFoundException {
        logger.info("--------------------" + Thread.currentThread().getStackTrace()[1] .getMethodName()
                + "--------------------");
        // 请求消息头
        requestHeaders = new HashMap<String, String>();
        v2ReqHeaders.setRequestHeaders(requestHeaders, DUOQU_SDK_APP_KEY, "2", imei, token, Byte.valueOf("1"),
                Byte.valueOf("3"));
        logger.info("RequestHeaders:" + ProtocolUtil.map_transform_json(v2ReqHeaders.getRequestHeaders()));
        request.setHeaders(requestHeaders);

        //请求消息体
        Map<String, Object> requestBody=setRequestBodyMap();
        logger.info("RequestBody:" + ProtocolUtil.map3_transform_json(requestBody));

        //通用请求消息体
        pubRequestBodyMap = new HashMap<String, String>();
        v2PublicReqBody.setRequestBodyMap(pubRequestBodyMap);

        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.putAll(v2ReqHeaders.getRequestHeaders());//请求消息头
        treeMap.putAll(v2PublicReqBody.getRequestBodyMap());//通用请求消息体

        String signKey = MStringUtil.getStringAfter10(requestHeaders.get("dp")) + MStringUtil.getStringAfter10(DUOQU_SDK_RSAPRVKEY);
        String keyValues = SignUtil.treeMapToString(treeMap, signKey);
        String si = MD5Util.md5Crypt(keyValues);
        pubRequestBodyMap.put("si", si);
        logger.info("PublicRequestBody:" + ProtocolUtil.map_transform_json(v2PublicReqBody.getRequestBodyMap()));

        mRequestBodyMap = new TreeMap<String, String>();
        mRequestBodyMap.putAll(v2PublicReqBody.getRequestBodyMap());
        mRequestBodyMap.putAll( (Map)requestBody);

        JSONObject reqBodyJson = new JSONObject();
        reqBodyJson.putAll(mRequestBodyMap);

        // 对请求消息体进行压缩加密
        byte nz = Byte.valueOf(requestHeaders.get("nz"));
        byte crypt = Byte.valueOf(requestHeaders.get("crypt"));

        byte[] reqBodyBytes = reqBodyJson.toJSONString().getBytes(charset);
        reqBodyBytes = ProtocolUtil.compress_encrypt_baseV2(reqBodyBytes, nz, crypt, aesKeyBytes, aesIVBytes);
        HttpURLConnection conn = HttpURLConnectionUtil.sendMethod(request.getUrl(), request.getHeaders(), reqBodyBytes);

        try {
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                logger.info(""+responseCode);
                // 对响应消息体进行解密解压
                Map<String, List<String>> responseHeaders = conn.getHeaderFields();
                logger.info("ResponseHeaders:" + ProtocolUtil.map2_transform_json(responseHeaders));
                byte deNz = 0;
                byte deCrypt = 0;
                if (responseHeaders.get("nz") != null) {
                    deNz = Byte.valueOf(responseHeaders.get("nz").get(0));
                }
                if (responseHeaders.get("crypt") != null) {
                    deCrypt = Byte.valueOf(responseHeaders.get("crypt").get(0));
                }
                byte[] responseBytes = HttpURLConnectionUtil.receiveMethod(conn);
                responseBytes = ProtocolUtil.decrypt_decompress_baseV2(responseBytes, deNz, deCrypt, aesKeyBytes, aesIVBytes);
                logger.info("ResponseBody:" + new String(responseBytes, charset));
                Map<String,String> mapTemp=new HashMap<>();
                mapTemp.put("CASENAME",casename);
                mapTemp.put("PROJECTNAME","新企业资料_Identify");
                mapTemp.put("APINAME","/business/pubinfo/identify");
                mapTemp.put("REQUESTBODY",ProtocolUtil.map3_transform_json(requestBody));
                mapTemp.put("REQUESTHEADER",ProtocolUtil.map_transform_json(v2ReqHeaders.getRequestHeaders()));
                mapTemp.put("RESPONSEBODY",new String(responseBytes, charset));
                mapTemp.put("ENVIRONMENT","0");
                mapTemp.put("CHAN",chan);
                mapTemp.put("IMEI",imei);
                List<Map<String,String>> list=new ArrayList<>();
                list.add(mapTemp);
//                int sum=InsertSqlUtil.executeInsert(list);
//                logger.info("插入数据"+sum+"条");
            }else{
                logger.info(""+responseCode);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
