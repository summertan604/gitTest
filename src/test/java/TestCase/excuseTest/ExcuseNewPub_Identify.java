package TestCase.excuseTest;

import TestCase.InsertData.NewPub_Identify;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xy.Util.*;
import com.xy.V2PubClass.V2PublicReqBody;
import com.xy.V2PubClass.V2ReqHeaders;
import com.xy.V2PubClass.V2TokenTest;
import com.xy.onlineteam.common.utility.Base64;
import com.xy.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ExcuseNewPub_Identify extends ConstDefineUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcuseNewPub_Identify.class);
    private static Charset charset = Charset.forName("UTF-8");
    private V2ReqHeaders v2ReqHeaders = new V2ReqHeaders();
    private V2PublicReqBody v2PublicReqBody = new V2PublicReqBody();
    private HttpClientRequest request = new HttpClientRequest();
    private Map<String, String> requestHeaders;
    private Map<String, String> requestBodyMap;
    private Map<String, Object> requestBodyMap1;
    private Map<String, String> pubRequestBodyMap;
    private TreeMap<String, String> mRequestBodyMap;
    private String DUOQU_SDK_APP_KEY;
    private String DUOQU_SDK_RSAPRVKEY;

    String sql;   //xml中读出的sql语句
    String urlPrefix;   //测试地址前缀
    String xlsxPath;   //数据结果存放地址
    /**
     * 通过读取properties文件中的值组装sql
     */
    @BeforeClass()
    @Parameters({"CONDITION", "URL", "XLSXPATH"})
    public void beforeClass(String CONDITION, String URL, String XLSXPATH) {
//        this.urlPrefix = prop.getProperty(PROPERTIES_URL);//取config.properties中的URL
//        this.xlsxPath=prop.getProperty(PROPERTIES_XLSXPATH); //取config.properties中的XLSXPATH
//        //取config.properties中的sql语句条件值
//        String condition = prop.getProperty(PROPERTIES_CONDITION);
        this.urlPrefix = URL;//取config.properties中的URL
        this.xlsxPath=XLSXPATH; //取config.properties中的XLSXPATH
//        //取config.properties中的sql语句条件值
        String condition = CONDITION;
        SelectSqlUtil sqlForSelect = new SelectSqlUtil();
        sqlForSelect.setSql(condition); //组装查询sql
        this.sql = sqlForSelect.getSql();
        logger.info(sql);
    }


    @DataProvider(name = "testData")
    private Iterator<Object[]> getData() {
        try {
            Connection conn = new MySqlUtil().connectMysql();
            return new ExecuteSelectUtil(conn, sql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Test(dataProvider = "testData")
    public void test(Map<String, String> dataMap){
        //getToken
        Map<String, String> appKey = AppKeyDoList.getAppKey(dataMap.get("CHAN"));
        DUOQU_SDK_APP_KEY = appKey.get("DUOQU_SDK_APP_KEY");
        DUOQU_SDK_RSAPRVKEY = appKey.get("DUOQU_SDK_RSAPRVKEY-2");

        Map<String,String> tokenMap=new V2TokenTest().getDetail(new V2TokenTest().getResponseBody(DUOQU_SDK_APP_KEY, "http://sdkapiv2.bizport.cn/token"));
        String token = tokenMap.get("token");
        byte[] aesKeyBytes = Base64.decode(tokenMap.get("aesKeyBytes"));
        byte[] aesIVBytes = Base64.decode(tokenMap.get("aesIVBytes"));


        // 请求消息头
        requestHeaders = new HashMap<String, String>();
        v2ReqHeaders.setRequestHeaders(requestHeaders, DUOQU_SDK_APP_KEY, "2", dataMap.get("IMEI"), token, Byte.valueOf("1"),
                Byte.valueOf("3"));
        logger.info("RequestHeaders:" + ProtocolUtil.map_transform_json(v2ReqHeaders.getRequestHeaders()));

        String url = urlPrefix + dataMap.get("APINAME");
        logger.info("测试地址:" + url);
        logger.info("用例编号:" + dataMap.get("CASENO"));
        logger.info("用例名称:" + dataMap.get("CASENAME"));
        logger.info("请求头:" + dataMap.get("REQUESTHEADER"));
        logger.info("请求体:" + dataMap.get("REQUESTBODY"));
        Reporter.log("======================start=========================" );
        Reporter.log("测试地址:" + url);
        Reporter.log("用例编号:" + dataMap.get("CASENO"));
        Reporter.log("用例名称:" + dataMap.get("CASENAME"));
        Reporter.log("请求头:" + ProtocolUtil.map_transform_json(v2ReqHeaders.getRequestHeaders()));
        Reporter.log("请求体:" + dataMap.get("REQUESTBODY"));

        // 对请求消息体进行压缩加密
        byte nz = Byte.valueOf(requestHeaders.get("nz"));
        byte crypt = Byte.valueOf(requestHeaders.get("crypt"));

        byte[] reqBodyBytes = dataMap.get("REQUESTBODY").getBytes(charset);
        reqBodyBytes = ProtocolUtil.compress_encrypt_baseV2(reqBodyBytes, nz, crypt, aesKeyBytes, aesIVBytes);
        long startTime = System.currentTimeMillis();
        HttpURLConnection conn = HttpURLConnectionUtil.sendMethod(url, requestHeaders, reqBodyBytes);
        long endTime = System.currentTimeMillis();
        long addTime = endTime - startTime;

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
                Reporter.log("======================返回体=========================" );
                Reporter.log("响应体:" + new String(responseBytes, charset));
                Reporter.log("响应耗时:" + addTime);

                Map<String,String>  result=new jsonCompare().compareJson(JSONObject.parseObject(new String(responseBytes, charset)),JSONObject.parseObject(dataMap.get("RESPONSEBODY")),null);
                String[]  excelHead= {"用例编号","用例名","是否一致","测试返回","断言内容","测试存在，断言不存在","断言存在，测试不存在","不一致的值"};
                Map<String, String> dataForExcel = new HashMap<String, String>();
                dataForExcel.put("用例编号", dataMap.get("CASENO") );
                dataForExcel.put("用例名", dataMap.get("CASENAME"));
                dataForExcel.put("是否一致", result.get("tag")=="1"?"否":"是");
                dataForExcel.put("测试返回", new String(responseBytes, charset));
                dataForExcel.put("断言内容", dataMap.get("RESPONSEBODY"));
                dataForExcel.put("测试存在，断言不存在", result.get("inTest"));
                dataForExcel.put("断言存在，测试不存在", result.get("inFormal"));
                dataForExcel.put("不一致的值", result.get("differenceValue"));
                List<Map> list = new ArrayList<Map>();
                list.add(dataForExcel);
                WriteExcel.writeExcel(list, xlsxPath,excelHead);
                Assert.assertEquals(result.get("tag"),"0", "不匹配");
                Reporter.log("是否一致" +result.get("tag")=="1"?"否":"是");
                Reporter.log("======================end=========================" );
            }else{
                logger.info(""+responseCode);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
