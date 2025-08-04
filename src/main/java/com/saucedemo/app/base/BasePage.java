package com.saucedemo.app.base;

import com.saucedemo.app.utils.MobileGestures;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Abstract base page class providing common Appium page operations.
 * Implements UserGestures for gesture support.
 */
public abstract class BasePage implements MobileGestures {
    /**
     * The Appium driver instance for interacting with the mobile app.
     */
    protected final AppiumDriver DRIVER;

    /**
     * FluentWait instance for waiting on conditions.
     */
    protected final FluentWait<AppiumDriver> wait;

    /**
     * Constructs a BasePage with the given AppiumDriver.
     *
     * @param driver the AppiumDriver instance, must not be null
     */
    protected BasePage(AppiumDriver driver) {
        this.DRIVER = driver;
        this.wait = new FluentWait<>(this.DRIVER)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(2));
    }

    /**
     * Finds a single element using the given By locator.
     *
     * @param locator the AppiumBy locator
     * @return the found WebElement
     */
    protected WebElement getElement(By locator) {
        return DRIVER.findElement(locator);
    }

    /**
     * Clicks on the given WebElement.
     *
     * @param element the WebElement to tap
     */
    protected void click(WebElement element) {
        element.click();
    }

    /**
     * Taps on the given WebElement.
     *
     * @param element the WebElement to tap
     */
    protected void tap(WebElement element) {
        tap(DRIVER, element);
    }

    /**
     * Clears and enters text into the given WebElement.
     *
     * @param element the WebElement to enter text into
     * @param text    the text to enter
     */
    protected void enterText(WebElement element, String text) {
        element.clear();
        element.click();
        addDelay(1);
        element.sendKeys(text);
    }

    /**
     * Gets the inner text of an element
     *
     * @param element the WebElement to check
     * @return inner text of an element
     */
    protected String getElementText(WebElement element) {
        return element.getText();
    }

    /**
     * Scrolls into view for the given locator depending on platform.
     *
     * @param androidLocator the locator for Android
     * @param iosLocator     the locator for iOS
     */
    protected void scrollIntoView(By androidLocator, By iosLocator) {
        if (DRIVER instanceof AndroidDriver) {
            DRIVER.findElement(androidLocator);
        } else {
            //scrollDown(DRIVER, element);
            //iosScroll(DRIVER, iosLocator, "predicateString");
        }
    }

    /**
     * Scrolls the screen in the specified direction.
     *
     * @param direction the direction to scroll ("up" or "down")
     */
    protected void scroll(String direction) {
        if (direction.equalsIgnoreCase("down"))
            scrollDown(DRIVER);
        else
            scrollUp(DRIVER);
    }

    /**
     * Checks if the given element is displayed within the specified timeout.
     *
     * @param element          the WebElement to check
     * @param timeoutInSeconds timeout in seconds
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element, int timeoutInSeconds) {
        try {
            return wait.withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .until(driver -> element.isDisplayed());
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the element located by the given locator is displayed within the specified timeout.
     *
     * @param locator          the By locator
     * @param timeoutInSeconds timeout in seconds
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator, int timeoutInSeconds) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Verifies that the given element is displayed, throws AssertionError with errorMessage if not.
     *
     * @param element     the WebElement to verify
     * @param errorMessage error message if not displayed
     */
    protected void verifyElementDisplayed(WebElement element, String errorMessage) {
        verifyElementDisplayed(element, errorMessage, 10);
    }

    /**
     * Verifies that the given element is displayed within the specified timeout.
     *
     * @param element          the WebElement to verify
     * @param errorMessage     error message if not displayed
     * @param timeoutInSeconds timeout in seconds
     */
    protected void verifyElementDisplayed(WebElement element, String errorMessage, int timeoutInSeconds) {
        wait.withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .ignoring(NoSuchElementException.class).withMessage(errorMessage)
                .until(driver -> element.isDisplayed());
    }

    /**
     * Verifies that the given element is clickable, throws AssertionError with errorMessage if not.
     *
     * @param element      the WebElement to verify
     * @param errorMessage error message if not clickable
     */
    protected void verifyElementClickable(WebElement element, String errorMessage) {
        verifyElementClickable(element, errorMessage, 10);
    }

    /**
     * Verifies that the given element is clickable within the specified timeout.
     *
     * @param element          the WebElement to verify
     * @param errorMessage     error message if not clickable
     * @param timeoutInSeconds timeout in seconds
     */
    protected void verifyElementClickable(WebElement element, String errorMessage, int timeoutInSeconds) {
        wait.withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .ignoring(NoSuchElementException.class).withMessage(errorMessage)
                .until(driver -> element.isDisplayed() && element.isEnabled());
    }

    /**
     * Asserts that the element located by the given locator is displayed.
     *
     * @param locator      the By locator
     * @param errorMessage error message if not displayed
     */
    protected void validateElementDisplayed(By locator, String errorMessage) {
        Assert.assertTrue(isElementDisplayed(locator, 5), errorMessage);
    }

    /**
     * Asserts that the given element is displayed.
     *
     * @param element      the WebElement to validate
     * @param errorMessage error message if not displayed
     */
    protected void validateElementDisplayed(WebElement element, String errorMessage) {
        Assert.assertTrue(isElementDisplayed(element, 5), errorMessage);
    }

    protected void addDelay(int timeInSecs) {
        try {
            Thread.sleep(timeInSecs * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
