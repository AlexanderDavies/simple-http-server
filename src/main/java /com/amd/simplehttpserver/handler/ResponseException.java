package com.amd.simplehttpserver.handler;

public class ResponseException extends Exception {
    HttpStatus status;

    public ResponseException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }
}

