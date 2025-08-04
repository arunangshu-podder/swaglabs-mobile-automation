package com.saucedemo.app.controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggerManager {
    private static final ThreadLocal<Logger> loggerThreadLocal = ThreadLocal.withInitial(() ->
            LogManager.getLogger(Thread.currentThread().getName()));

    /**
     * Gets the logger instance for the current thread.
     *
     * @return Logger object for the current thread
     */
    public static Logger getLogger() {
        return loggerThreadLocal.get();
    }

    /**
     * Logs an info-level message.
     *
     * @param message The message to be logged
     */
    public static void info(String message) {
        getLogger().info(message);
    }

    /**
     * Logs an error-level message.
     *
     * @param message The error message to be logged
     */
    public static void error(String message) {
        getLogger().error(message);
    }

    /**
     * Logs a debug-level message.
     *
     * @param message The debug message to be logged
     */
    public static void debug(String message) {
        getLogger().debug(message);
    }
}
