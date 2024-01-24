package com.amd.simplehttpserver.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final static int MAX_HEADER_BUFFER_SIZE = 8192;
    private final InputStream inputStream;
    private String method;
    private String protocol;
    private String path;
    private final Map<String, String> headers;
    private Object body;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        this.headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    private void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    private void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

    private void setBody(Object body) {
        this.body = body;
    }

    public void build() throws IOException, ResponseException {
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

        // Parse headers
        parseHeaders(headerBuffer, headerEnd);

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

    private void parseHeaders(byte[] headerBuffer, int headerEnd) throws ResponseException {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(headerBuffer, 0, headerEnd), StandardCharsets.UTF_8));

            String firstLine = reader.readLine();

            String[] tokens = firstLine.split(" ");

            setMethod(tokens[0]);

            //Check if there is a path
            if (tokens.length == 3) {
                setPath(tokens[1]);
                setProtocol(tokens[2]);
            } else {
                setProtocol(tokens[1]);
            }

            String headerLine;
            while ((headerLine = reader.readLine()) != null) {
                String[] headerLineTokens = headerLine.split(":");
                String key = headerLineTokens[0].trim();
                String value = headerLineTokens[1].trim();
                headers.put(key, value);
            }


        } catch (Exception ex) {
            throw new ResponseException("Unable to decode HTTP headers", HttpStatus.BAD_REQUEST);
        }

    }

}
