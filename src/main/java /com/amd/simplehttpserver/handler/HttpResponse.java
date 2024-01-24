package com.amd.simplehttpserver.handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class HttpResponse <E> {

    private static String protocol = "HTTP/1.1";
    private OutputStream outputStream;
    private final Map<String, String> headers;
    private HttpStatus status;
    private Object body;

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public HttpResponse(Object body, OutputStream outputStream) {
        this.headers = new HashMap<>();
        this.body = body;
        this.outputStream = outputStream;

    }

    public String getHeader(String key) {
        if (this.headers.containsKey(key)) {
            return this.headers.get(key).toLowerCase();
        }
        return null;
    }

    public void appendHeader(StringBuilder sb, String header, String value) {
        sb.append(header + ": " + value + "\r\n");
    }

    public String convertBodyToJson() {
        // TO DO: Implement JSON conversion logic
        // For now, return an empty JSON object as a placeholder
        return "{}";
    }

    public void send() throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.outputStream, StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();

        sb.append(protocol).append(" ").append(this.status.getStatusCode()).append(" ").append("OK").append("\r\n");

        for(String key : this.headers.keySet()){
            appendHeader(sb, key, this.headers.get(key));
        }

        if (getHeader("content-type") == null) {
            appendHeader(sb, "Content-Type", "application-json");
        }


        if (getHeader("connection") == null) {
           appendHeader(sb,"Connection", "keep-alive");
        }

        if(getHeader("date") == null) {
            appendHeader(sb, "Date", sdf.format(new Date()));
        }

        // TO DO: Handle Transfer-Encoding header e.g. GZIP vs Chunked

        //TO DO: Handle cookies

        appendHeader(sb, "Content-Length", "2");

        sb.append("\r\n");

        //TO DO: handle response body properly!
        sb.append(body);

        // Send the data and close the streams
        writer.write(sb.toString());
        writer.flush();
    }

}
