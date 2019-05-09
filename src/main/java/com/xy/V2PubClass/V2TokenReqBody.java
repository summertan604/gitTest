package com.xy.V2PubClass;



import com.xy.utility.MSignaturer;

import java.io.Serializable;
import java.util.Map;

public class V2TokenReqBody implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> requestBodyMap;

	public Map<String, String> getRequestBodyMap() {
		return requestBodyMap;
	}

	public void setRequestBodyMap(Map<String, String> requestBodyMap, String x, String p, String ai) {
		requestBodyMap.put("x", MSignaturer.sha256Encode(x));
		requestBodyMap.put("p", MSignaturer.sha256Encode(p));
		requestBodyMap.put("ai", MSignaturer.sha256Encode(ai));
		requestBodyMap.put("timestamp", Long.toString(System.currentTimeMillis()));
		this.requestBodyMap = requestBodyMap;
	}
}
