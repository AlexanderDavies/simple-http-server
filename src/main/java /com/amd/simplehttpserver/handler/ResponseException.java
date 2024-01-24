package com.amd.simplehttpserver.handler;

public class ResponseException extends Exception {
    HttpStatus status;

    public ResponseException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}

