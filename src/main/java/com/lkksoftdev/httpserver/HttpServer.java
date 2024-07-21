package com.lkksoftdev.httpserver;

import com.lkksoftdev.httpserver.config.Configuration;
import com.lkksoftdev.httpserver.config.ConfigurationManager;
import com.lkksoftdev.httpserver.config.HttpConfigurationException;
import com.lkksoftdev.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance()
                .loadConfigurationFile("src/main/resources/serverconfig.json");

        Configuration config = ConfigurationManager.getInstance().getConfiguration();

        LOGGER.info("Server webroot {}", config.getWebroot());
        
        Thread serverListenerThread;

        try {
            serverListenerThread = new ServerListenerThread(config.getPort(), config.getRequestLimit());
            serverListenerThread.start();
        } catch (IOException e) {
            throw new HttpConfigurationException("Error occurred while trying to start server", e);
        }

    }
}