package com.saucedemo.app.pages;

import com.saucedemo.app.base.BasePage;
import com.saucedemo.app.controller.ReportsManager;
import com.saucedemo.app.objects.Product;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class OrderConfirmationPage extends BasePage {
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"CHECKOUT: COMPLETE!\")")
    @iOSXCUITFindBy(iOSNsPredicate = "label == 'CHECKOUT: COMPLETE!' AND type == 'XCUIElementTypeOther'")
    private WebElement checkoutCompleteBanner;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"THANK YOU FOR YOU ORDER\")")
    private WebElement thankYouMessage;

    public OrderConfirmationPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void validateNavigationToOrderConfirmationPage() {
        try {
            validateElementDisplayed(checkoutCompleteBanner,
                    "Failed to navigate to Checkout: Complete Page. Expected banner not displayed.");
            ReportsManager.passStep("Successfully navigated to Checkout: Complete Page.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to navigate to Checkout: Complete Page. Reason: " + e.getMessage());
            Assert.fail("Failed to navigate to Checkout: Complete Page. Reason: " + e.getMessage());
        }
    }

    public void validateThankYouMessageDisplayed() {
        try {
            validateElementDisplayed(thankYouMessage, "Thank You message not displayed.");
            ReportsManager.passStep("Thank You message displayed successfully.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to validate Thank You message. Reason: " + e.getMessage());
            Assert.fail("Failed to validate Thank You message. Reason: " + e.getMessage());
        }
    }
}
