package TestCase.excuseTest;

import com.alibaba.fastjson.JSON;
import com.xy.Util.*;
import com.xy.onlineteam.common.utility.AESUtils;
import com.xy.onlineteam.common.utility.ZipUtil;
import com.xy.utility.HttpClientRequest;
import com.xy.utility.HttpURLConnectionUtil;
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

public class ExcuseOldPub extends ConstDefineUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcuseOldPub.class);
    private static Charset charset = Charset.forName("UTF-8");

    private static String SECRET_AES_KEY = "dEWCQ0o8woY0QNxn";
    private static final byte[] AES_IV = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF, (byte) 0xA9, (byte) 0xB7, (byte) 0xC8, (byte) 0xD6, (byte) 0xE3, (byte) 0xF1,
            (byte) 0x1F, (byte) 0xFE };

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


    /**
     * 完整的测试数据，会以map的形式传递到测试方法里
     */
    @Test(dataProvider = "testData")
    public void test(Map<String, String> dataMap)  {
        String url = urlPrefix + dataMap.get("APINAME");
        HttpClientRequest request = new HttpClientRequest();
        request.setUrl(url);
        logger.info("测试地址:" + url);
        logger.info("用例编号:" + dataMap.get("CASENO"));
        logger.info("用例名称:" + dataMap.get("CASENAME"));
        logger.info("请求头:" + dataMap.get("REQUESTHEADER"));
        logger.info("请求体:" + dataMap.get("REQUESBODY"));
        Reporter.log("======================start=========================" );
        Reporter.log("测试地址:" + url);
        Reporter.log("用例编号:" + dataMap.get("CASENO"));
        Reporter.log("用例名称:" + dataMap.get("CASENAME"));
        Reporter.log("请求头:" + dataMap.get("REQUESTHEADER"));
        Reporter.log("请求体:" + dataMap.get("REQUESBODY"));

        byte[] reqBodyBytes = dataMap.get("REQUESBODY").getBytes(charset);
        reqBodyBytes = ZipUtil.gzip(reqBodyBytes);
        reqBodyBytes = AESUtils.aesEncrypt(reqBodyBytes, SECRET_AES_KEY.getBytes(charset), AESUtils.ALGORITHM, AES_IV);
        long startTime = System.currentTimeMillis();
        HttpURLConnection conn = HttpURLConnectionUtil.sendMethod(url,(Map)JSON.parse(dataMap.get("REQUESTHEADER")), reqBodyBytes);
        long endTime = System.currentTimeMillis();
        long addTime = endTime - startTime;
        try {
            int rspCode = conn.getResponseCode();
            logger.debug("rspCode:" + rspCode);
            logger.debug("rspHeaders:" + conn.getHeaderFields());
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
                logger.debug("response:" + rspBody);
                Reporter.log("======================返回体=========================" );
                Reporter.log("响应体:" + rspBody);
                Reporter.log("响应耗时:" + addTime);
                Map<String,String>  result=new xmlCompare().xmlCompare(rspBody,dataMap.get("RESPONSEBODY"));
                String[]  excelHead= {"用例编号","用例名","是否一致","测试返回","断言内容","测试存在，断言不存在","断言存在，测试不存在"};
                Map<String, String> dataForExcel = new HashMap<String, String>();
                dataForExcel.put("用例编号", dataMap.get("CASENO") );
                dataForExcel.put("用例名", dataMap.get("CASENAME"));
                dataForExcel.put("是否一致", result.get("tag")=="1"?"否":"是");
                dataForExcel.put("测试返回", rspBody);
                dataForExcel.put("断言内容", dataMap.get("RESPONSEBODY"));
                dataForExcel.put("测试存在，断言不存在", result.get("inTest"));
                dataForExcel.put("断言存在，测试不存在", result.get("inFormal"));
                List<Map> list = new ArrayList<Map>();
                list.add(dataMap);
                WriteExcel.writeExcel(list, xlsxPath,excelHead);
                Assert.assertEquals(result.get("tag"),"0", "不匹配");
                Reporter.log("是否一致" +result.get("tag")=="1"?"否":"是");
                Reporter.log("======================end=========================" );
            } else {
                Assert.assertTrue(false, Integer.toString(rspCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

