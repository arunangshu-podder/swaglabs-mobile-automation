package com.saucedemo.app.controller;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for reporting test execution events using ReportsManager.
 */
public class ListenerManager implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        LoggerManager.info("TestNG suite started: " + context.getSuite().getName());
        ReportsManager.initiateReporter("Automation Report", "SwagLabs App Tests");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription();
        LoggerManager.info("Test started: " + testName);
        ExtentTest test = ReportsManager.getExtentReports().createTest(testName);
        ReportsManager.setTest(test);
        ReportsManager.logInfo("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerManager.info("Test passed: " + result.getMethod().getDescription());
        ReportsManager.passTest("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String message = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed";
        LoggerManager.error("Test failed: " + result.getMethod().getDescription() + " - " + message);
        ReportsManager.failTest(message);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerManager.info("Test skipped: " + result.getMethod().getDescription());
        ReportsManager.logInfo("Test skipped: " + result.getMethod().getDescription());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LoggerManager.debug("Test failed but within success percentage: " + result.getMethod().getDescription());
        // Not used
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerManager.info("TestNG suite finished: " + context.getSuite().getName());
        ReportsManager.buildReport();
    }
}
