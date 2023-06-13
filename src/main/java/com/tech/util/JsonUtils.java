package com.tech.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * 2023/6/7
 */
public class JsonUtils {

    static ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> tClass) throws JsonProcessingException {
        return mapper.readValue(json, tClass);
    }

    public static <K, V> Map<K, V> jsonToMap(String json, Class<K> kClass, Class<V> vClass) throws JsonProcessingException {
        return mapper.readValue(json, new TypeReference<Map<K, V>>() {});
    }

    public static <T> List<T> jsonToList(String json, Class<T> tClass) throws JsonProcessingException {
        return mapper.readValue(json, new TypeReference<List<T>>() {});
    }

    public static Map<String, Object> objToMap(Object obj) throws JsonProcessingException {
        String json = toJsonString(obj);
        return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    }

    public static <K, V> Map<K, V> objToMap(Object obj, Class<K> kClass, Class<V> vClass) throws JsonProcessingException {
        String json = toJsonString(obj);
        return mapper.readValue(json, new TypeReference<Map<K, V>>() {});
    }

    public static <T> List<T> objToList(Object obj, Class<T> tClass) throws JsonProcessingException {
        String json = toJsonString(obj);
        return mapper.readValue(json, new TypeReference<List<T>>() {});
    }

}
