package com.saucedemo.app.utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility interface for common user gestures on mobile elements.
 */
public interface MobileGestures {

    /**
     * Performs a tap gesture on the given element.
     *
     * @param driver  the Appium driver instance
     * @param element the WebElement to tap
     */
    default void tap(AppiumDriver driver, WebElement element) {
        executeGestureByElementLocation(driver, element,
                driver instanceof AndroidDriver ? "mobile: clickGesture" : "mobile: tap");
    }

    /**
     * Performs a double tap gesture on the given element.
     *
     * @param driver  the Appium driver instance
     * @param element the WebElement to double tap
     */
    default void doubleTap(AppiumDriver driver, WebElement element) {
        executeGesture(driver, element,
                driver instanceof AndroidDriver ? "mobile: doubleClickGesture" : "mobile: doubleTap");
    }

    /**
     * Performs a long press gesture on the given element.
     *
     * @param driver  the Appium driver instance
     * @param element the WebElement to long press
     */
    default void longPress(AppiumDriver driver, WebElement element) {
        executeGesture(driver, element,
                driver instanceof AndroidDriver ? "mobile: longClickGesture" : "mobile: touchAndHold");
    }

    default void iosScroll(AppiumDriver driver, String locatorStr, String using) {
        System.out.println("Locator found: " + locatorStr);
        Map<String, Object> params = new HashMap<>();
        params.put(using, locatorStr);
        params.put("direction", "down");
        params.put("percent", 1.0);
        executeGesture(driver, "mobile:scroll", params);
    }

    /**
     * Scrolls down on the screen based on hardcoded coordinates.
     *
     * @param driver the Appium driver instance
     */
    default void scrollDown(AppiumDriver driver) {
        Map<String, Object> params = new HashMap<>();
        params.put("top", 1900);
        params.put("left", 450);
        params.put("width", 400);
        params.put("height", 1300);
        params.put("direction", "down");
        params.put("percent", 0.75);
        if (driver instanceof AndroidDriver)
            executeGesture(driver, "mobile:scrollGesture", params);
        else
            executeGesture(driver, "mobile:scroll", params);
    }

    /**
     * Scrolls up on the screen based on hardcoded coordinates.
     *
     * @param driver the Appium driver instance
     */
    default void scrollUp(AppiumDriver driver) {
        Map<String, Object> params = new HashMap<>();
        params.put("top", 1900);
        params.put("left", 450);
        params.put("width", 400);
        params.put("height", 1300);
        params.put("direction", "up");
        params.put("percent", 0.75);
        if (driver instanceof AndroidDriver)
            executeGesture(driver, "mobile:scrollGesture", params);
        else
            executeGesture(driver, "mobile:scroll", params);
    }

    /**
     * Executes a mobile gesture using JavascriptExecutor.
     *
     * @param driver      the Appium driver instance
     * @param element     the WebElement to perform gesture on
     * @param gestureName the mobile gesture command
     */
    default void executeGesture(AppiumDriver driver, WebElement element, String gestureName) {
        if (element == null) {
            throw new IllegalArgumentException("WebElement must not be null.");
        }
        String elementId = ((RemoteWebElement) element).getId();
        ((JavascriptExecutor) driver).executeScript(
                gestureName, ImmutableMap.of("elementId", elementId));
    }

    /**
     * Executes a mobile gesture using JavascriptExecutor and the given parameters.
     *
     * @param driver      the Appium driver instance
     * @param gestureName the mobile gesture command
     * @param params      additional parameters for the gesture
     */
    default Object executeGesture(AppiumDriver driver, String gestureName, Map<String, Object> params) {
        return ((JavascriptExecutor) driver).executeScript(gestureName, params);
    }

    /**
     * Executes a mobile gesture using JavascriptExecutor with additional parameters.
     *
     * @param driver      the Appium driver instance
     * @param element     the WebElement to perform gesture on
     * @param gestureName the mobile gesture command
     * @param params      additional parameters for the gesture
     */
    default Object executeGesture(AppiumDriver driver, WebElement element, String gestureName, Map<String, Object> params) {
        if (element == null) {
            throw new IllegalArgumentException("WebElement must not be null.");
        }
        String elementId = ((RemoteWebElement) element).getId();
        params.put("elementId", elementId);
        return executeGesture(driver, gestureName, params);
    }

    /**
     * Executes a mobile gesture using JavascriptExecutor and the co-ordinates of the element.
     *
     * @param driver      the Appium driver instance
     * @param element     the WebElement to perform gesture on
     * @param gestureName the mobile gesture command
     */
    default void executeGestureByElementLocation(AppiumDriver driver, WebElement element, String gestureName) {
        if (element == null) {
            throw new IllegalArgumentException("WebElement must not be null.");
        }
        int centerX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
        int centerY = element.getLocation().getY() + (element.getSize().getHeight() / 2);
        ((JavascriptExecutor) driver).executeScript(
                gestureName, ImmutableMap.of("x", centerX, "y", centerY));
    }
}