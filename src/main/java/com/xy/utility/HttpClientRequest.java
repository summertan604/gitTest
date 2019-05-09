package com.xy.utility;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

public class HttpClientRequest {

	private String url;
	private HttpUriRequest httpMethod;
	private Map<String, String> headers;
	private byte[] body;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpUriRequest getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpUriRequest httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
}
