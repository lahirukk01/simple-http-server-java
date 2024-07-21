package com.lkksoftdev.httpserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private static final List<String> ALLOWED_VERSIONS = Arrays.asList("HTTP/1.0", "HTTP/1.1", "HTTP/2.0");

    private HttpMethod method;
    private String path;
    private String resourcePath;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParameters = new HashMap<>();
    private String body;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        String[] pathParts = path.split("\\?");

        if (pathParts.length == 2) {
            this.resourcePath = pathParts[0];
            String[] queryParts = pathParts[1].split("&");

            for (String queryPart : queryParts) {
                String[] queryParam = queryPart.split("=");
                queryParameters.put(queryParam[0], queryParam[1]);
            }
        } else {
            this.resourcePath = path;
        }
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (!ALLOWED_VERSIONS.contains(version)) {
            throw new HttpParserException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addQueryParameter(String key, String value) {
        queryParameters.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", queryParameters=" + queryParameters +
                ", resourcePath='" + resourcePath + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
