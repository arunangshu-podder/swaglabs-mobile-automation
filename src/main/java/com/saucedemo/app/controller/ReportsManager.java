package com.saucedemo.app.controller;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages ExtentReports lifecycle, logging, and screenshot attachment for test reporting.
 */
public class ReportsManager {
    private static final ExtentReports extentReports = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORT_PATH = System.getProperty("user.dir") + "/target/reports";

    // Private constructor to prevent instantiation
    private ReportsManager() {}

    /**
     * Initializes the ExtentReports reporter with document title and report name.
     *
     * @param documentTitle the title of the report document
     * @param reportName    the name of the report
     */
    public static void initiateReporter(String documentTitle, String reportName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH + "/ExtentReport.html");
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setReportName(reportName);
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Returns the singleton ExtentReports instance.
     *
     * @return ExtentReports instance
     */
    public static ExtentReports getExtentReports() {
        return extentReports;
    }

    /**
     * Returns the current thread's ExtentTest instance.
     *
     * @return ExtentTest for current thread
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Sets the ExtentTest instance for the current thread.
     *
     * @param test ExtentTest instance
     */
    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    /**
     * Removes the ExtentTest instance for the current thread.
     */
    public static void removeTest() {
        extentTest.remove();
    }

    /**
     * Logs a passed step with message.
     *
     * @param message step description
     */
    public static void passStep(String message) {
        LoggerManager.info(message);
        getTest().log(Status.PASS, message);
        addScreenshotToReport();
    }

    /**
     * Logs a failed step with message.
     *
     * @param message step description
     */
    public static void failStep(String message) {
        LoggerManager.error(message);
        getTest().log(Status.FAIL, message);
        addScreenshotToReport();
    }

    /**
     * Logs an informational message.
     *
     * @param message info message
     */
    public static void logInfo(String message) {
        LoggerManager.info(message);
        getTest().info(message);
    }

    /**
     * Marks the test as passed and attaches a screenshot.
     *
     * @param message pass message
     */
    public static void passTest(String message) {
        getTest().pass(message);
        addScreenshotToReport();
    }

    /**
     * Marks the test as failed and attaches a screenshot.
     *
     * @param message fail message
     */
    public static void failTest(String message) {
        getTest().fail(message);
        addScreenshotToReport();
    }

    /**
     * Flushes the ExtentReports and writes the report to disk.
     */
    public static void buildReport() {
        getExtentReports().flush();
        LoggerManager.info("Generated report: " + REPORT_PATH + "/ExtentReport.html");
    }

    /**
     * Captures a screenshot and attaches it to the current test report.
     */
    private static void addScreenshotToReport() {
        String filePath = takeScreenshot();
        getTest().addScreenCaptureFromPath(filePath);
    }

    /**
     * Captures a screenshot and saves it to the report directory.
     *
     * @return the absolute path of the screenshot file
     */
    private static String takeScreenshot() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String screenshotPath = REPORT_PATH + "/screenshot_" + timestamp + ".png";
        return takeScreenshot(screenshotPath);
    }

    /**
     * Captures a screenshot and saves it to the specified path.
     *
     * @param screenshotPath the path to save the screenshot
     * @return the absolute path of the screenshot file
     */
    private static String takeScreenshot(String screenshotPath) {
        File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        File destn = new File(screenshotPath);
        try {
            Files.copy(src.toPath(), destn.toPath());
            LoggerManager.info("Screenshot saved at: " + screenshotPath);
        } catch (IOException e) {
            LoggerManager.error("Error while capturing screenshot: " + e.getMessage());
            throw new RuntimeException("Error while capturing screenshot.", e);
        }
        return destn.getName();
    }
}
