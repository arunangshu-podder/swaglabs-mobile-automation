package com.saucedemo.app.controller;

import com.google.common.collect.ImmutableMap;
import com.saucedemo.app.utils.Constants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.JavascriptExecutor;

public class AppManager {

    // ThreadLocal instance for thread-safe AppManager usage
    private static final ThreadLocal<AppManager> threadLocalInstance = ThreadLocal.withInitial(AppManager::new);

    /**
     * Returns the thread-local AppManager instance.
     */
    public static AppManager getInstance() {
        return threadLocalInstance.get();
    }

    /**
     * Thread-safe wrapper to launch the app.
     */
    public static void launchAppThreadSafe() {
        if (Constants.ENABLE_PERFECTO) {
            LoggerManager.info("App launch is not required for Perfecto testing. Skipping app launch.");
            return;
        }
        getInstance().launchApp();
    }

    /**
     * Thread-safe wrapper to close the app.
     */
    public static void closeAppThreadSafe() {
        getInstance().closeApp();
    }

    /**
     * Thread-safe wrapper to remove the app.
     */
    public static void removeAppThreadSafe() {
        if (Constants.ENABLE_PERFECTO) {
            LoggerManager.info("App removal is not required for Perfecto testing. Skipping app removal.");
            return;
        }
        getInstance().removeApp();
    }

    /**
     * Launches the application on the device using platform-specific commands.
     * For Android, starts the specified activity; for iOS, launches the app by bundleId.
     * Throws UnsupportedOperationException if the driver type is unsupported.
     */
    private void launchApp() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver) {
            String activity = Constants.APP_PACKAGE + "/" + Constants.APP_ACTIVITY;
            LoggerManager.debug("Launching Android app with activity: " + activity);
            ((JavascriptExecutor) driver).executeScript(
                    "mobile: startActivity", ImmutableMap.of("intent", activity));
        } else if (driver instanceof IOSDriver) {
            String bundleId = Constants.BUNDLE_ID;
            LoggerManager.debug("Launching iOS app with bundleId: " + bundleId);
            ((JavascriptExecutor) driver).executeScript(
                    "mobile: launchApp", ImmutableMap.of("bundleId", bundleId));
        } else {
            LoggerManager.error("Unsupported driver type: " + driver.getClass().getName());
            throw new UnsupportedOperationException("Unsupported driver type: " + driver.getClass().getName());
        }
    }

    /**
     * Terminates the application on the device using platform-specific commands.
     * For Android, terminates by package name; for iOS, by bundleId.
     * Throws UnsupportedOperationException if the driver type is unsupported.
     */
    private void closeApp() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver) {
            LoggerManager.debug("Terminating Android app: " + Constants.APP_PACKAGE);
            ((AndroidDriver) driver).terminateApp(Constants.APP_PACKAGE);
        } else if (driver instanceof IOSDriver) {
            LoggerManager.debug("Terminating iOS app: " + Constants.BUNDLE_ID);
            ((IOSDriver) driver).terminateApp(Constants.BUNDLE_ID);
        } else {
            LoggerManager.error("Unsupported driver type: " + driver.getClass().getName());
            throw new UnsupportedOperationException("Unsupported driver type: " + driver.getClass().getName());
        }
    }

    /**
     * Removes the application from the device using platform-specific commands.
     * For Android, removes by package name; for iOS, by bundleId.
     * Throws UnsupportedOperationException if the driver type is unsupported.
     */
    public void removeApp() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver) {
            LoggerManager.debug("Removing Android app: " + Constants.APP_PACKAGE);
            ((AndroidDriver) driver).removeApp(Constants.APP_PACKAGE);
        } else if (driver instanceof IOSDriver) {
            LoggerManager.debug("Removing iOS app: " + Constants.BUNDLE_ID);
            ((IOSDriver) driver).removeApp(Constants.BUNDLE_ID);
        } else {
            LoggerManager.error("Unsupported driver type: " + driver.getClass().getName());
            throw new UnsupportedOperationException("Unsupported driver type: " + driver.getClass().getName());
        }
    }
}
