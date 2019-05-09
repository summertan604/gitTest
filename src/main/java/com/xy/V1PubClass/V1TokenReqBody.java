package com.xy.V1PubClass;

import java.io.Serializable;
import java.util.Map;

public class V1TokenReqBody implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, String> requestBodyMap;

	public Map<String, String> getRequestBodyMap() {
		return requestBodyMap;
	}

	public void setRequestBodyMap(Map<String, String> requestBodyMap, String DUOQU_SDK_APP_KEY) {
		requestBodyMap.put("secdata", DUOQU_SDK_APP_KEY + "_" + System.currentTimeMillis());
		this.requestBodyMap = requestBodyMap;
	}
}
