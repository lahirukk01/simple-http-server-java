package com.lkksoftdev.httpserver.config;

public class Configuration {
    private int port;
    private int requestLimit;
    private String webroot;

    public int getPort() {
        return port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(int requestLimit) {
        this.requestLimit = requestLimit;
    }
}
