package TestCase.InsertData;

import com.alibaba.fastjson.JSONObject;
import com.xy.Util.InsertSqlUtil;
import com.xy.onlineteam.common.utility.AESUtils;
import com.xy.onlineteam.common.utility.ZipUtil;
import com.xy.utility.EncryptUtil;
import com.xy.utility.HttpURLConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;

import static com.xy.V0PubClass.V0ReqHeaders.reqHeaders;

public class OldPub {


    private static Class<?> className = OldPub.class;
    private static final Logger log = LoggerFactory.getLogger(className);
    private static final Charset charset = Charset.forName("UTF-8");


    private static String SECRET_AES_KEY = "dEWCQ0o8woY0QNxn";
    private static final byte[] AES_IV = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF, (byte) 0xA9, (byte) 0xB7, (byte) 0xC8, (byte) 0xD6, (byte) 0xE3, (byte) 0xF1,
            (byte) 0x1F, (byte) 0xFE };
    public String reqUrl() {

//        String url = "http://192.168.101.214:9998/pubNumService";//测试地址
//		String url = "http://test-ali-1.bizport.cn:9998/pubNumService";    //外网测试服地址http://
        String url = "http://pubserver5.bizport.cn:9998/pubNumService";    //外网正式服地址
        return url;
    }

    public static String reqBody(String num, String sign, String areaCode,String userAreaCode) {
        String reqBody = "<?xml version='1.0' encoding='utf-8'?><QueryPubInfoRequest>" +
                "<num>"+num+"</num>" +
                "<sign>"+EncryptUtil.string2SHA256StrJava(sign)+"</sign>" +
                "<version>1234567</version>" +
                "<areaCode>"+areaCode+"</areaCode>" +
                "<userAreaCode>"+userAreaCode+"</userAreaCode></QueryPubInfoRequest>";
//		String reqBody = "<?xml version='1.0' encoding='utf-8'?><QueryPubInfoRequest>\n" +
//				"<allNums>" +
//				"<num ver=\"1\">123456</num>" +
//				"<num ver=\"1\">10691234568598</num>" +
//				"<num ver=\"1\">10086</num>" +
//				"</allNums>" +
//				"<version>1</version>" +
//				"<sign></sign>" +
//				"<cnum></cnum>" +
//				"<areaCode>GD</areaCode>" +
//				"<userAreaCode></userAreaCode>" +
//				"<iccid></iccid>" +
//				"<type>1</type>" +
//				"</QueryPubInfoRequest>";
//		String reqBody = "<?xml version='1.0' encoding='utf-8'?><QueryPubInfoRequest>" +
//				"<allNums>" +
//				"<num ver=\"1\" sign=\"中国联通\">10010</num>" +
//				"<num ver=\"1\" sign=\"深圳学而思\">10691367328107180</num>" +
//				"</allNums>" +
//				"<version></version>" +
//				"<cnum></cnum>" +
//				"<areaCode>CN</areaCode>" +
//				"<userAreaCode></userAreaCode>" +
//				"<iccid></iccid>" +
//				"<type>1</type>" +
//				"</QueryPubInfoRequest>";
//		String reqBody = "<?xml version='1.0' encoding='utf-8'?><QueryPubInfoRequest>" +
//				"<num sg=\"001603c2b69544cd1875766744a7d8ed691993491e36ada4c41713029b44f9bd\">10657516195553</num>" +
//				"<sign>海通证券</sign>" +
//				"<cnum></cnum>" +
//				"<areaCode>GD</areaCode>" +
//				"<iccid></iccid>" +
//				"<type>1</type>" +
//				"</QueryPubInfoRequest>";
//		String reqBody = "<?xml version='1.0' encoding='utf-8'?><QueryPubInfoRequest>" +
//				"<allNums type=\"1\">" +
//				"<num sign=\"邮储银行1558\">1069097685266828</num>" +
//				"</allNums>" +
//				"<sign></sign>" +
//				"<cnum></cnum>" +
//				"<areaCode>GD</areaCode>" +
//				"<iccid></iccid>" +
//				"<type>1</type>" +
//				"</QueryPubInfoRequest>";


        return reqBody;
    }

    @BeforeClass
    public void beforeClass() {
        log.debug("----------start----------");

    }


    //	@Test(invocationCount=100,threadPoolSize=10)
    @Test
    public void testcase003_compress1_nz1_encrypt2() throws SQLException, ClassNotFoundException {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName());
        String url = reqUrl();
        String channel="MEIZU2";
        String imei="863784027870935";
        HashMap<String, String> reqHeaders = reqHeaders(channel,imei);
        JSONObject jsonReqHeaders = new JSONObject();
        jsonReqHeaders.putAll(reqHeaders);
        log.debug("jsonReqHeaders:" + jsonReqHeaders.toJSONString());
        String casename="企业支持HUAWEI2HUAWEI3_请求HUAWEI_无数据返回";
        String reqBody = reqBody("10690271135999999227", "", "GD","GD");//1069068574513  10690685* 特步官方旗舰店
        // 先压缩后加密
        byte[] reqBodyBytes = reqBody.getBytes(charset);
        reqBodyBytes = ZipUtil.gzip(reqBodyBytes);
        reqBodyBytes = AESUtils.aesEncrypt(reqBodyBytes, SECRET_AES_KEY.getBytes(charset), AESUtils.ALGORITHM, AES_IV);

        HttpURLConnection conn = HttpURLConnectionUtil.sendMethod(url, reqHeaders, reqBodyBytes);
        log.debug("url:" + url);
        log.debug("reqHeaders:" + reqHeaders);
        log.debug("reqBody:" + reqBody);
        try {
            int rspCode = conn.getResponseCode();
            log.debug("rspCode:" + rspCode);
            log.debug("rspHeaders:" + conn.getHeaderFields());
            if (rspCode == HttpURLConnection.HTTP_OK) {
                byte[] rspData = HttpURLConnectionUtil.receiveMethod(conn);
                byte[] data = null;
                try {
                    data = AESUtils.aesDecrypt(rspData, SECRET_AES_KEY.getBytes(charset), AESUtils.ALGORITHM, AES_IV);
                    data = ZipUtil.unGzip(data);
                } catch (Exception e) {
                    data = rspData;
                }
                String rspBody = new String(data, charset);
                log.debug("response:" + rspBody);
                Map<String,String> mapTemp=new HashMap<>();
                mapTemp.put("CASENAME",casename);
                mapTemp.put("PROJECTNAME","旧企业资料");
                mapTemp.put("APINAME","pubNumService");
                mapTemp.put("REQUESTBODY",reqBody);
                mapTemp.put("REQUESTHEADER",jsonReqHeaders.toJSONString());
                mapTemp.put("RESPONSEBODY",rspBody);
                mapTemp.put("ENVIRONMENT","0");
                mapTemp.put("CHAN",channel);
                mapTemp.put("IMEI",imei);
                List<Map<String,String>> list=new ArrayList<>();
                list.add(mapTemp);
//                int sum=InsertSqlUtil.executeInsert(list);
//                log.info("插入数据"+sum+"条");

            } else {
                Assert.assertTrue(false, Integer.toString(rspCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
