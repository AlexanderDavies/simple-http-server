package com.amd.simplehttpserver.handler;

import java.util.Map;

public interface HttpRequest<T> {

    public String getProtocol();
    public HttpMethod getMethod();
    public String getPath();
    public String[] getPathParams();
    public Map<String, String> getQueryParams();
    public Map<String, String> getHeaders();
    public T getBody();
    public String getContentType();
}
