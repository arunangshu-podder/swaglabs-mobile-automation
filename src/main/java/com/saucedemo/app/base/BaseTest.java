package com.saucedemo.app.base;

import com.saucedemo.app.controller.*;
import com.saucedemo.app.utils.Constants;
import com.saucedemo.app.utils.FileUtils;
import com.saucedemo.app.utils.JsonUtils;
import com.saucedemo.app.utils.PropertyUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Base test class for initializing and cleaning up Appium driver and service.
 * Provides common setup and teardown logic for all test classes.
 */
public abstract class BaseTest {

    /**
     * Clears previous reports.
     * Starts the Appium service.
     * Loads the config file before the tests start.
     */
    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        try {
            LoggerManager.info("Clearing reports from previous execution...");
            FileUtils.clearFolder("target/reports");

            LoggerManager.info("Loading config properties file...");
            PropertyUtils.loadConfigProperties();

            LoggerManager.info("Starting Appium service...");
            AppiumServiceManager.startAppiumService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads device profile, initializes the driver, and launches the app (if pre-installed) before each test.
     *
     * @param method the test method about to be executed
     */
    @BeforeMethod(alwaysRun = true)
    public void testSetup(Method method){

        try {
            LoggerManager.info("Loading device profile JSON file...");
            JsonUtils.loadDeviceProfile(Constants.DEVICE_PROFILE);

            LoggerManager.info("Launching device...");
            //DeviceManager.launchDevice(Constants.getDeviceName());

            LoggerManager.info("Setting up driver for test: " + method.getName());
            initDriver();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Constants.IS_APP_PRE_INSTALLED) {
            LoggerManager.info("App is pre-installed. Launching app...");
            AppManager.launchAppThreadSafe();
        }
    }

    /**
     * Closes the app, removes it, kills the driver and closes the device after each test.
     */
    @AfterMethod(alwaysRun = true)
    public void testTeardown() {
        LoggerManager.info("Tearing down test. Closing app and killing driver.");
        AppManager.closeAppThreadSafe();
        //AppManager.removeAppThreadSafe();
        DriverManager.killDriver();
        //closeDevice();
    }

    /**
     * Stops the Appium service after the test suite completes.
     */
    @AfterSuite(alwaysRun = true)
    public static void tearDown() {
        LoggerManager.info("Stopping Appium service after suite...");
        AppiumServiceManager.stopAppiumService();
    }

    /**
     * Initializes the AppiumDriver and sets the implicit wait timeout.
     * Driver instance is retrieved from DriverManager.
     */
    private void initDriver() {
        AppiumDriver driver = DriverManager.getDriver();
        LoggerManager.info("Initializing Appium driver...");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Closes the device after test execution.
     * For Android: Shuts down all running emulators using adb.
     * For iOS: Shuts down all running simulators using simctl.
     * Logs errors and throws RuntimeException if shutdown fails.
     */
    public void closeDevice() {
        try {
            if (Constants.ENABLE_PERFECTO) {
                LoggerManager.info("Perfecto testing enabled. No device shutdown required.");
                return;
            }
            if (DriverManager.getDriver() instanceof AndroidDriver) {
                LoggerManager.info("Shutting down all emulators.");
                Runtime.getRuntime().exec("adb emu kill").waitFor();
            } else if (DriverManager.getDriver() instanceof IOSDriver) {
                LoggerManager.info("Shutting down all simulators.");
                Runtime.getRuntime().exec("xcrun simctl shutdown all").waitFor();
            } else {
                LoggerManager.info("Unknown driver type. No device shutdown performed.");
            }
        } catch (IOException | InterruptedException e) {
            LoggerManager.error("Error closing device: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
