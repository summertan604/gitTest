package com.xy.V2PubClass;

import com.xy.onlineteam.common.utility.Coder;
import com.xy.onlineteam.common.utility.MD5Util;
import com.xy.utility.MSignaturer;

import java.io.Serializable;
import java.util.Map;

public class V2ReqHeaders implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> requestHeaders;

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}



	public void setRequestHeaders(Map<String, String> requestHeaders, String DUOQU_SDK_APP_KEY,String apiver, String imei,
			String token, byte nz, byte crypt) {
		requestHeaders.put("chan", DUOQU_SDK_APP_KEY);
		requestHeaders.put("apiver", apiver);//取值为2或201
		requestHeaders.put("sdkver", "201607011616");
		requestHeaders.put("uiver", "201502020000");
		requestHeaders.put("dp", MD5Util.md5Crypt(Coder.sha256ToHexString(imei)));
		requestHeaders.put("uid", MSignaturer.sha256Encode("359225071700694"));
		requestHeaders.put("tk", token);
		requestHeaders.put("nz", Byte.toString(nz));
		requestHeaders.put("crypt", Byte.toString(crypt));
		requestHeaders.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.0.0; M6 Note Build/NRD90M)");
		this.requestHeaders = requestHeaders;
	}
}
