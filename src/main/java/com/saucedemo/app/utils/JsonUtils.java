package com.saucedemo.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucedemo.app.controller.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for loading and accessing JSON device profile files.
 */
public interface JsonUtils {

    static final Map<String, Object> jsonData = new ConcurrentHashMap<>();

    /**
     * Loads a JSON file from src/test/resources/device-profiles/{fileName} into memory.
     * @param fileName Name of the JSON file (e.g., "ios/iPhone_15.json" or "android/Pixel_7.json")
     * @throws IOException if the file cannot be read or parsed
     */
    public static void loadDeviceProfile(String fileName) throws IOException {
        String filePath = "src/test/resources/device-profiles/" + fileName;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);
        if (!file.exists()) {
            LoggerManager.error("JSON file not found: " + filePath);
            throw new IOException("JSON file not found: " + filePath);
        }
        Map<String, Object> map = mapper.readValue(file, Map.class);
        jsonData.clear();
        jsonData.putAll(map);
        LoggerManager.info("Loaded JSON device profile: " + filePath);
    }

    /**
     * Returns the value for the given key from the loaded JSON file.
     * @param key The key to look up
     * @return The value as Object, or null if not found
     */
    public static Object get(String key) {
        return jsonData.get(key);
    }

    /**
     * Returns the value for the given key as a String.
     * @param key The key to look up
     * @return The value as String, or null if not found
     */
    public static String getString(String key) {
        Object value = jsonData.get(key);
        return value != null ? value.toString() : null;
    }
}
