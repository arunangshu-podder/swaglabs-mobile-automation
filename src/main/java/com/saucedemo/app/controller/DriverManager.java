package com.saucedemo.app.controller;

import com.saucedemo.app.utils.Constants;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages thread-safe AppiumDriver instances for parallel execution.
 */
public class DriverManager {
    /**
     * Thread-safe AppiumDriver instance for each thread.
     */
    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private DriverManager() {}

    /**
     * Returns the current AppiumDriver instance for the thread, initializing it if necessary.
     *
     * @return AppiumDriver instance
     * @throws MalformedURLException if the Appium server URL is malformed
     */
    public static AppiumDriver getDriver() {
        if (DRIVER.get() == null) {
            LoggerManager.debug("No driver found for current thread. Initializing new driver for platform: " + Constants.getPlatformName());
            DRIVER.set(getDriver(Constants.getPlatformName()));
        }
        return DRIVER.get();
    }

    /**
     * Returns a AppiumDriver instance for the specified platform type.
     *
     * @param platformType the platform type ("android" or "ios")
     * @return AppiumDriver instance
     * @throws MalformedURLException if the Appium server URL is malformed
     * @throws IllegalArgumentException if the platform type is unsupported
     */
    public static AppiumDriver getDriver(String platformType) {
        String url = getURL();
        LoggerManager.info("Creating driver for platform: " + platformType + " at URL: " + url);
        try {
            if (platformType.equalsIgnoreCase("android")) {
                return getAndroidDriver(url);
            } else if (platformType.equalsIgnoreCase("ios")) {
                return getIOSDriver(url);
            } else {
                LoggerManager.error("Unsupported platform type: " + platformType);
                throw new IllegalArgumentException("Unsupported platform type: " + platformType);
            }
        } catch (MalformedURLException e) {
            LoggerManager.error("Malformed Appium server URL: " + url + " - " + e.getMessage());
            throw new RuntimeException("Malformed Appium server URL: " + url, e);
        }
    }

    /**
     * Quits and removes the AppiumDriver instance for the current thread.
     */
    public static void killDriver() {
        if (DRIVER.get() != null) {
            LoggerManager.info("Quitting and removing driver for current thread.");
            DRIVER.get().quit();
            DRIVER.remove();
        } else {
            LoggerManager.error("No driver to quit for current thread.");
        }
    }

    /**
     * Initializes and returns an AndroidDriver instance.
     *
     * @param url the Appium server URL
     * @return AndroidDriver instance
     * @throws MalformedURLException if the URL is malformed
     */
    private static AppiumDriver getAndroidDriver(String url) throws MalformedURLException {
        LoggerManager.debug("Initializing AndroidDriver with URL: " + url);
        UiAutomator2Options options = new UiAutomator2Options();
        if (!Constants.ENABLE_PERFECTO) {
            options.setDeviceName(Constants.getDeviceName());
            options.setPlatformName(Constants.getPlatformName());
            //options.setPlatformVersion(Constants.getPlatformVersion());
            options.setAutomationName(Constants.getAutomationName());
            //options.setNoReset(false);
            //options.setFullReset(false);
            options.setNewCommandTimeout(Duration.ofSeconds(5));
            options.setAutoGrantPermissions(true);
            options.setAvdLaunchTimeout(Duration.ofSeconds(60));
            options.setAvdReadyTimeout(Duration.ofSeconds(60));
            options.setNewCommandTimeout(Duration.ofMinutes(5));
            options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(120));
            if (!Constants.IS_APP_PRE_INSTALLED) {
                File apkFile = new File(Constants.APK_FILE_PATH);
                LoggerManager.debug("Setting APK file for AndroidDriver: " + apkFile.getAbsolutePath());
                options.setApp(apkFile.getAbsolutePath());
                options.setAppPackage(Constants.APP_PACKAGE);
                options.setAppActivity(Constants.APP_ACTIVITY);
            }
        } else {
            options.setPlatformName(Constants.getPlatformName());
            // Wrap vendor-specific caps in 'perfecto:options'
            Map<String, Object> perfectoOptions = new HashMap<>();
            perfectoOptions.put("securityToken", Constants.Perfecto.PERFECTO_TOKEN);
            perfectoOptions.put("manufacturer", Constants.Perfecto.getManufacturer());
            perfectoOptions.put("model", Constants.getDeviceName());
            perfectoOptions.put("resolution", Constants.Perfecto.getResolution());
            perfectoOptions.put("platformVersion", Constants.getPlatformVersion());
            perfectoOptions.put("platformBuild", Constants.Perfecto.getPlatformBuild());
            perfectoOptions.put("app", "PRIVATE:Android.SauceLabs.Mobile.Sample.app.2.7.1.apk");
            perfectoOptions.put("autoLaunch", Constants.Perfecto.getAutoLaunch());
            perfectoOptions.put("enableAppiumBehavior", Constants.Perfecto.enableAppiumBehavior());

            options.setCapability("perfecto:options", perfectoOptions);
        }
        System.out.println("Configuration token: " + Constants.Perfecto.PERFECTO_TOKEN);
        AppiumDriver driver = new AndroidDriver(new URL(url), options);
        LoggerManager.info("Driver initialized successfully.");
        DRIVER.set(driver);
        return driver;
    }

    /**
     * Initializes and returns an IOSDriver instance.
     *
     * @param url the Appium server URL
     * @return IOSDriver instance
     * @throws MalformedURLException if the URL is malformed
     */
    private static AppiumDriver getIOSDriver(String url) throws MalformedURLException {
        LoggerManager.debug("Initializing IOSDriver with URL: " + url);
        XCUITestOptions options = new XCUITestOptions();
        options.setDeviceName(Constants.getDeviceName());
        options.setPlatformName(Constants.getPlatformName());
        options.setPlatformVersion(Constants.getPlatformVersion());
        //options.setAutomationName(Constants.getAutomationName());
        //options.setNoReset(false);
        //options.setFullReset(true);
        options.setNewCommandTimeout(Duration.ofSeconds(5));
        options.setWdaLaunchTimeout(Duration.ofSeconds(120));
        options.setWdaConnectionTimeout(Duration.ofSeconds(120));
        if (!Constants.IS_APP_PRE_INSTALLED) {
            File appFile = new File(Constants.APP_FILE_PATH);
            LoggerManager.debug("Setting app file for IOSDriver: " + appFile.getAbsolutePath());
            options.setApp(appFile.getAbsolutePath());
            options.setBundleId(Constants.BUNDLE_ID);
        }

        AppiumDriver driver = new IOSDriver(new URL(url), options);
        LoggerManager.info("Driver initialized successfully.");
        DRIVER.set(driver);
        return driver;
    }

    private static String getURL() {
        return Constants.ENABLE_PERFECTO ? Constants.Perfecto.PERFECTO_URL :
                "http://" + Constants.APPIUM_IP_ADDRESS + ":" + Constants.APPIUM_PORT;
    }
}
