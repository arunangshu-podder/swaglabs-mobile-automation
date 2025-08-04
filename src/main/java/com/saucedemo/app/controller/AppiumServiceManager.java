package com.saucedemo.app.controller;

import com.saucedemo.app.utils.Constants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;

/**
 * Manages the lifecycle of the Appium server service.
 */
public class AppiumServiceManager {
    /**
     * Singleton instance of AppiumDriverLocalService.
     */
    private static volatile AppiumDriverLocalService service = null;
    private static final String APPIUM_LOG_FILEPATH = System.getProperty("user.dir") + "/target/reports/appium_server.log";

    /**
     * Private constructor to prevent instantiation.
     */
    private AppiumServiceManager() {}

    /**
     * Initializes the Appium service with the configured parameters.
     */
    public static void initAppiumService() {
        File logFile = new File(APPIUM_LOG_FILEPATH);
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(Constants.APPIUM_JS_FILEPATH))
                .usingDriverExecutable(new File(Constants.APPIUM_NODE_EXECUTABLE))
                .withIPAddress(Constants.APPIUM_IP_ADDRESS)
                .usingPort(Constants.APPIUM_PORT)
                .withArgument(() -> "--log-no-color")
                .withLogFile(logFile)
                .withLogOutput(null)
                .build();
    }

    /**
     * Starts the Appium service if it is not already running.
     * Throws a RuntimeException if the service is already running or fails to start.
     */
    public static void startAppiumService() {
        if (Constants.ENABLE_PERFECTO) {
            LoggerManager.info("Appium service is not required for Perfecto testing. Skipping service start.");
            return;
        }
        initAppiumService();
        if (service != null && !service.isRunning()) {
            service.start();
            if (!service.isRunning()) {
                throw new RuntimeException("Failed to start Appium service.");
            }
        } else if (service != null && service.isRunning()) {
            throw new RuntimeException("Appium service is already running.");
        } else {
            throw new RuntimeException("Appium service initialization failed.");
        }
    }

    /**
     * Stops the Appium service if it is running.
     * Throws a RuntimeException if the service is not running or has already been stopped.
     */
    public static void stopAppiumService() {
        if (Constants.ENABLE_PERFECTO) {
            LoggerManager.info("Appium service is not required for Perfecto testing. Skipping service stop.");
            return;
        }
        if (service != null && service.isRunning()) {
            service.stop();
        } else if (service != null) {
            throw new RuntimeException("Appium service is not running or has already been stopped.");
        } else {
            throw new RuntimeException("Appium service was not initialized.");
        }
    }
}
