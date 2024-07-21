package com.lkksoftdev.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class Json {
    private static final ObjectMapper objectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static JsonNode toJson(Object obj) {
        return objectMapper.valueToTree(obj);
    }

    private static String toJsonString(Object obj, boolean pretty) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writer();

        if (pretty) {
            writer = writer.withDefaultPrettyPrinter();
        }

        return writer.writeValueAsString(obj);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return toJsonString(node, false);
    }

    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return toJsonString(node, true);
    }
}
