package com.lkksoftdev.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lkksoftdev.httpserver.util.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationManager {
    private static ConfigurationManager instance = null;
    private static Configuration configuration;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            throw new HttpConfigurationException("Configuration has not been loaded yet.");
        }
        return configuration;
    }

    public void loadConfigurationFile(String filename) {
        // Load configuration from file
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        JsonNode node;
        try {
            node = Json.parse(content);
        } catch (IOException e) {
            throw new HttpConfigurationException("Error parsing configuration file", e);
        }

        try {
            configuration = Json.fromJson(node, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error converting the json node", e);
        }

    }
}
