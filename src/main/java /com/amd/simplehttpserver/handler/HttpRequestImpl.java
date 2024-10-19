package com.amd.simplehttpserver.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestImpl<T> implements HttpRequest<T> {

    private final static int MAX_HEADER_BUFFER_SIZE = 8192;
    private final InputStream inputStream;
    private String method;
    private String protocol;
    private String path;
    private String[] pathParams;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private T body;

    public HttpRequestImpl(InputStream inputStream) {
        this.inputStream = inputStream;
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new String[0];
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String[] getPathParams() {
        return pathParams.clone();
    }

    @Override
    public Map<String, String> getQueryParams() {
        return Map.copyOf(queryParams);
    }

    @Override
    public Map<String, String> getHeaders() {
        // update to deep copy
        return Map.copyOf(headers);
    }

    @Override
    public T getBody() {
        return body;
    }

    @Override
    public String getContentType() {
        return "";
    }

    protected void build() throws IOException, ResponseException {
        //  Calculate header length.
        byte[] headerBuffer = new byte[MAX_HEADER_BUFFER_SIZE];
        int headerEnd = -1;
        int byteRead = -1;
        int numBytesRead = 0;

        this.inputStream.mark(MAX_HEADER_BUFFER_SIZE);

        // Read InputStream once to ensure client connection has not closed
        byteRead = this.inputStream.read(headerBuffer, 0, MAX_HEADER_BUFFER_SIZE);

        numBytesRead++;

        if (byteRead == -1) {
            throw new IOException("Client connection has been closed");
        }

        while (byteRead > 0) {
            numBytesRead += byteRead;
            headerEnd = findHeaderEnd(headerBuffer);
            if (headerEnd > 0) {
                break;
            }
            byteRead = this.inputStream.read(headerBuffer, numBytesRead, MAX_HEADER_BUFFER_SIZE);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(headerBuffer, 0, headerEnd), StandardCharsets.UTF_8));

        parseRequestLine(reader);

        parseHeaders(reader);

        // Handle body for post, put, patch requests
        if (getMethod().equals(HttpMethods.POST.toString())
                || getMethod().equals(HttpMethods.PATCH.toString())
                || getMethod().equals(HttpMethods.PUT.toString())) {

            // Calculate body length
            this.inputStream.reset();
            this.inputStream.skip(headerEnd + 4);

            // read body into buffer

        }
    }

    private int findHeaderEnd(byte[] headerBuffer) {

        for (int b = 0; b < headerBuffer.length; b++) {
            if (headerBuffer[b] == '\r' && headerBuffer[b + 1] == '\n' && headerBuffer[b + 2] == '\r' && headerBuffer[b + 3] == '\n') {
                return b;
            }
        }
        return -1;
    }

    private void parsePath(String firstLine) {
        String[] tokens = firstLine.split(" ");

        method = tokens[0];

        //Check if there is a path
        if (tokens.length == 3) {
            path = tokens[1];
            protocol = tokens[2];
        } else {
            protocol = tokens[1];
        }
    }

    private void parseQueryParams() {
        if (path != null) {
            String queryString = path.substring(path.indexOf("?") + 1);

            String[] queryArray = queryString.split("&");

            for (String query : queryArray) {
                String[] keyValue = query.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        String headerLine;
        while ((headerLine = reader.readLine()) != null) {
            String[] headerLineTokens = headerLine.split(":");
            String key = headerLineTokens[0].trim();
            String value = headerLineTokens[1].trim();
            headers.put(key, value);
        }
    }

    private void parseRequestLine(BufferedReader reader) throws IOException {

            parsePath(reader.readLine());

            parseQueryParams();

            //TO DO: parse path params;

    }

}
