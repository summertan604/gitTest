package com.xy.V2PubClass;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xy.onlineteam.common.utility.Base64;
import com.xy.utility.AppKeyDoList;
import com.xy.utility.HttpClientRequest;
import com.xy.utility.HttpURLConnectionUtil;
import com.xy.utility.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V2TokenTest {
	private static Logger logger = LoggerFactory.getLogger(V2TokenTest.class);
	private static Charset charset = Charset.forName("UTF-8");
	private V2ReqHeaders v2ReqHeaders = new V2ReqHeaders();
	private V2TokenReqBody v2TokenRequestBody = new V2TokenReqBody();
	private HttpClientRequest request = new HttpClientRequest();
	private Map<String, String> requestHeaders;
	private Map<String, String> requestBodyMap;
	private String token="";
	private String DUOQU_SDK_APP_KEY;
	private String DUOQU_SDK_RSAPRVKEY;

	public String getResponseBody(String chan,String token_url) {
		logger.info("--------------------" + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "--------------------");
		Map<String, String> appKey = AppKeyDoList.getAppKey(chan);
		String DUOQU_SDK_APP_KEY = appKey.get("DUOQU_SDK_APP_KEY");
		String DUOQU_SDK_RSAPRVKEY = appKey.get("DUOQU_SDK_RSAPRVKEY-2");

		request.setUrl(token_url);//http://sdkapiv2.bizport.cn/token
		logger.info("url:" + request.getUrl());

		requestHeaders = new HashMap<String, String>();
		v2ReqHeaders.setRequestHeaders(requestHeaders, DUOQU_SDK_APP_KEY, "2", "359225071700694",token, Byte.valueOf("1"),
				Byte.valueOf("2"));
		logger.info("RequestHeaders:" + ProtocolUtil.map_transform_json(v2ReqHeaders.getRequestHeaders()));
		request.setHeaders(requestHeaders);

		requestBodyMap = new HashMap<String, String>();
		v2TokenRequestBody.setRequestBodyMap(requestBodyMap, "898603142502921", "359225071700694", "fe68b42b");
		logger.info("RequestBody:" + ProtocolUtil.map_transform_json(v2TokenRequestBody.getRequestBodyMap()));

		// 对请求消息体进行压缩加密
		byte nz = Byte.valueOf(requestHeaders.get("nz"));
		byte crypt = Byte.valueOf(requestHeaders.get("crypt"));

		byte[] reqBodyBytes = ProtocolUtil.map_transform_json(requestBodyMap).getBytes(charset);
		reqBodyBytes = ProtocolUtil.compress_encrypt_tokenV2(reqBodyBytes, nz, crypt, DUOQU_SDK_RSAPRVKEY);
		request.setBody(ProtocolUtil.map_transform_json(requestBodyMap).getBytes(charset));
		HttpURLConnection conn = HttpURLConnectionUtil.sendMethod(request.getUrl(), request.getHeaders(), reqBodyBytes);

		// 对响应消息体进行解密解压
		Map<String, List<String>> responseHeaders = conn.getHeaderFields();
		logger.info("responseHeaders:" + ProtocolUtil.map2_transform_json(responseHeaders));
		byte deNz = 0;
		byte deCrypt = 0;
		if (responseHeaders.get("nz") != null) {
			deNz = Byte.valueOf(responseHeaders.get("nz").get(0));
		}
		if (responseHeaders.get("crypt") != null) {
			deCrypt = Byte.valueOf(responseHeaders.get("crypt").get(0));
		}
		byte[] responseBytes = HttpURLConnectionUtil.receiveMethod(conn);
		responseBytes = ProtocolUtil.decrypt_decompress_tokenV2(responseBytes, deNz, deCrypt, DUOQU_SDK_RSAPRVKEY);
		logger.info("responseBody:" + new String(responseBytes, charset));
		try {
			Assert.assertEquals(200, conn.getResponseCode());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return new String(responseBytes, charset);
	}

	public Map<String,String> getDetail(String tkResponseBody){
        JSONObject tkResponseBodyJson = JSON.parseObject(tkResponseBody);
        String token = JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("token").toString();
        String aesKeyBytes = JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("aeskey").toString();
        String aesIVBytes =JSON.parseObject(tkResponseBodyJson.get("body").toString()).get("iv").toString();
        Map<String,String> result=new HashMap<>();
        result.put("token",token);
        result.put("aesKeyBytes",aesKeyBytes);
        result.put("aesIVBytes",aesIVBytes);
        return result;
    }

}