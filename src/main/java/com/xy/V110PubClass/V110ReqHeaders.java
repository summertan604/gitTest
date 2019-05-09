package com.xy.V110PubClass;

import java.util.Map;

public class V110ReqHeaders {

    private Map<String, String> requestHeaders;

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders,String chan,String apiver) {
        requestHeaders.put("chan",chan);
        requestHeaders.put("apiver",apiver);
        this.requestHeaders = requestHeaders;
    }
}
