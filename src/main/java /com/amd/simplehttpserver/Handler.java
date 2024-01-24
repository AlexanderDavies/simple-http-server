package com.amd.simplehttpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class Handler implements Runnable {

    private InputStream inputStream;
    private int bytesRead = 0;

    public Handler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private int findHeaderEnd(byte[] headerBuffer) {
        int headerBufferLength = headerBuffer.length;

        for (int b = 0; b < headerBufferLength - 1; b++) {
            if (headerBuffer[b] == '\r' && headerBuffer[b + 1] == '\n') {
                return b + 3;
            }
        }
        return 0;
    }

    private void getHeaders() {

        int maxHeaderBufferSize = 8192;
        byte[] headerBuffer = new byte[maxHeaderBufferSize];
        int headerEnd = 0;

        while (headerEnd == 0) {
            try {
                bytesRead = inputStream.read(headerBuffer, bytesRead, maxHeaderBufferSize - bytesRead);

                if (bytesRead == -1) {
                    //TO DO, throw an exceptino
                }

                headerEnd = findHeaderEnd(headerBuffer);

                if (headerEnd > 0) {
                    bytesRead = headerEnd;
                    break;
                }

            } catch (IOException ex) {
                // TO DO
            }
        }

        //resize bufferArray for headers only
        headerBuffer = Arrays.copyOf(headerBuffer, headerEnd);

        //Parse headers into MAP<String, String>
        String headerString = Base64.getEncoder().encodeToString(headerBuffer);

        System.out.println(headerString);
    }


    private void getBody() {

        try {
            inputStream.reset();
            inputStream.skip(bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        //Get Headers
        getHeaders();


        // get headers

        // get body


    }
}
