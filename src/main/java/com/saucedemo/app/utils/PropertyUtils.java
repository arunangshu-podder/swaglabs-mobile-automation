package com.saucedemo.app.utils;

import com.saucedemo.app.controller.LoggerManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading configuration properties from Config.properties file.
 */
public interface PropertyUtils {
    /**
     * Holds the loaded configuration properties.
     */
    public static final Properties Config = new Properties();

    /**
     * Loads the configuration properties from the Config.properties file.
     * Ensures the input stream is closed and handles exceptions with logging.
     *
     * @throws IOException if the properties file cannot be loaded
     */
    public static void loadConfigProperties() throws IOException {
        try (FileInputStream fis = new FileInputStream("./Config.properties")) {
            Config.load(fis);
            LoggerManager.info("Config.properties loaded successfully.");
        } catch (IOException e) {
            LoggerManager.error("Failed to load Config.properties: " + e.getMessage());
            throw e;
        }
    }
}
