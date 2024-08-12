package com.example.model_predict.utils;

/**
 * @author Dr.Awe
 * @date 2024-07-26 10:44
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    // 将 Java 对象转换为 JSON 字符串
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    // 将 JSON 字符串转换为 Java 对象
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }

    // 将 JSON 字符串转换为 Java 对象，指定类型
    public static <T> T fromJson(String jsonString, com.fasterxml.jackson.core.type.TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }

    // 读取 JSON 字符串中的特定字段
    public static <T> T readField(String jsonString, String fieldName, Class<T> clazz) {
        try {
            return objectMapper.readTree(jsonString).get(fieldName).traverse(objectMapper.readerFor(clazz)).readValueAs(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error reading field from JSON", e);
        }
    }

    // 写入 Java 对象中的特定字段到 JSON 字符串
    public static <T> String writeField(Object obj, String fieldName, T value) {
        try {
            String jsonString = toJson(obj);
            ObjectNode objectNode = (ObjectNode) objectMapper.readTree(jsonString);
            objectNode.set(fieldName, objectMapper.valueToTree(value));
            return objectMapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            throw new RuntimeException("Error writing field to JSON", e);
        }
    }
}

