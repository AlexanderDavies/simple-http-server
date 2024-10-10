package com.amd.simplehttpserver.handler;

import java.util.Map;

public interface HttpRequest {

    public String getProtocol();
    public String getMethod();
    public String getPath();
    public String[] getPathParams();
    public Map<String, String> getQueryParams();
    public Map<String, String> getHeaders();
    public Object getBody();
    public String getContentType();
}
