package com.xy.V1PubClass;

import com.xy.utility.MSignaturer;

import java.io.Serializable;
import java.util.Map;

public class V1ReqHeaders implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> requestHeaders;

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders, String DUOQU_SDK_APP_KEY, String imei,
			String token, byte nz, byte crypt) {
		requestHeaders.put("appkey", DUOQU_SDK_APP_KEY);
		requestHeaders.put("token", token);
		requestHeaders.put("sdkversion", "201502020000");
		requestHeaders.put("uiversion", "201502020000");
		requestHeaders.put("nz", Byte.toString(nz));
		requestHeaders.put("crypt", Byte.toString(crypt));
		requestHeaders.put("xid", MSignaturer.sha256Encode("89860113859018800000"));// SHA256(ICCID)
		requestHeaders.put("usid", imei);// 手机IMEI的明文
		requestHeaders.put("uid", MSignaturer.sha256Encode(imei));// SHA256(手机imei)
		requestHeaders.put("uip", "192.168.101.3");// 用户IP
		this.requestHeaders = requestHeaders;
	}
}
