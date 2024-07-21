package com.lkksoftdev.httpserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private final HttpRequest request = new HttpRequest();

    private static final int SP = 32;
    private static final int CR = 13;
    private static final int LF = 10;

    private static final int REQUEST_LINE_MAX_LENGTH = 8192;
    private static final List<String> HTTP_METHODS_WITH_BODY = Arrays.asList("POST", "PUT", "PATCH");

    private void parseRequestLine(InputStreamReader reader) throws IOException {
        LOGGER.info("Parsing Request Line...");
        int requestLineSectionPointer = 0;

        StringBuilder requestLineBuilder = new StringBuilder();
        int charCode, charCount = 0;
        String token;

        while ((charCode = reader.read()) != -1 && (charCount++ < REQUEST_LINE_MAX_LENGTH)) {
            if (charCode == CR) {
                charCode = reader.read();
                if (charCode == LF) {
                    token = requestLineBuilder.toString();
                    requestLineBuilder.setLength(0);
                    request.setVersion(token);
                    requestLineSectionPointer++;
                    break;
                }
            } else if (charCode == SP) {
                token = requestLineBuilder.toString();
                requestLineBuilder.setLength(0);

                switch (requestLineSectionPointer) {
                    case 0:
                        request.setMethod(HttpMethod.valueOf(token));
                        break;
                    case 1:
                        request.setPath(token);
                        break;
                    default:
                        LOGGER.error("Invalid request line section pointer: {}", requestLineSectionPointer);
                        break;
                }

                requestLineSectionPointer++;
            } else {
                requestLineBuilder.append((char) charCode);
            }
        }

        if (charCount >= REQUEST_LINE_MAX_LENGTH) {
            throw new HttpParserException(HttpStatus.URI_TOO_LONG);
        }

        if (requestLineSectionPointer != 0 && requestLineSectionPointer != 3) {
            throw new HttpParserException(HttpStatus.BAD_REQUEST);
        }
    }

    private void parseHeaders(InputStreamReader reader) throws IOException {
        LOGGER.info("Parsing Headers...");

        StringBuilder headerLineBuilder = new StringBuilder();
        int charCode, charCount = 0;
        String token;

        while ((charCode = reader.read()) != -1 && (charCount++ < REQUEST_LINE_MAX_LENGTH)) {
            if (charCode == CR) {
                charCode = reader.read();
                if (charCode == LF) {
                    token = headerLineBuilder.toString();
                    headerLineBuilder.setLength(0);

                    if (token.isEmpty()) {
                        break;
                    }

                    String[] headerParts = token.split(": ");
                    request.addHeader(headerParts[0], headerParts[1]);
                }
            } else {
                headerLineBuilder.append((char) charCode);
            }
        }

        if (charCount >= REQUEST_LINE_MAX_LENGTH) {
            throw new HttpParserException(HttpStatus.URI_TOO_LONG);
        }
    }

    private void parseBody(InputStreamReader reader) throws IOException {
        LOGGER.info("Parsing Body...");

        int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));

        if (contentLength <= 0) {
            throw new HttpParserException(HttpStatus.BAD_REQUEST);
        }

        StringBuilder bodyBuilder = new StringBuilder();
        int charCode, charCount = 0;

        while ((charCode = reader.read()) != -1 && (charCount++ < contentLength)) {
            bodyBuilder.append((char) charCode);
        }

        request.setBody(bodyBuilder.toString());
    }

    public HttpRequest parseHttpRequest(InputStream in) throws IOException {
        LOGGER.info("Parsing HTTP Request...");

        InputStreamReader reader = new InputStreamReader(in);

        parseRequestLine(reader);

        if (request.getMethod() == null) return null;

        parseHeaders(reader);

        if (HTTP_METHODS_WITH_BODY.contains(request.getMethod().name()) &&
                request.getHeaders().containsKey("Content-Length") &&
                request.getHeaders().get("Content-Length") != null) {
            parseBody(reader);
        }

        return request;
    }
}
