package com.lkksoftdev.httpserver;

import com.lkksoftdev.httpserver.config.Configuration;
import com.lkksoftdev.httpserver.config.ConfigurationManager;

public class HttpServer {
    public static void main(String[] args) {
        System.out.println("Server starting...");

        ConfigurationManager.getInstance()
                .loadConfigurationFile("src/main/resources/serverconfig.json");

        Configuration configuration = ConfigurationManager.getInstance().getConfiguration();

        System.out.println("Server started on port " + configuration.getPort());
        System.out.println("Server webroot " + configuration.getWebroot());

    }
}