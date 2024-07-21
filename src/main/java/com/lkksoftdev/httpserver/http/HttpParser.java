package com.lkksoftdev.httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    public void parseHttpRequest(InputStream in) {
        LOGGER.info("Parsing HTTP Request...");
    }
}