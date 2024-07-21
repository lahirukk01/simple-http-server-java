package com.lkksoftdev.httpserver.http;

public class HttpParserException extends RuntimeException {
    private final int statusCode;

    public HttpParserException(HttpStatus status) {
        super(status.getMessage());
        this.statusCode = status.getCode();
    }
}
