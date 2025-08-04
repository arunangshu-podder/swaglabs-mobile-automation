package com.saucedemo.app.pages;

import com.saucedemo.app.base.BasePage;
import com.saucedemo.app.controller.ReportsManager;
import com.saucedemo.app.objects.User;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CheckoutInfoPage extends BasePage {
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"CHECKOUT: INFORMATION\")")
    @iOSXCUITFindBy(iOSNsPredicate = "label == 'CHECKOUT: INFORMATION' AND type == 'XCUIElementTypeOther'")
    private WebElement checkoutInfoBanner;

    @AndroidFindBy(accessibility = "test-First Name")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeTextField[`name == \"test-First Name\"`]")
    private WebElement firstNameTextField;

    @AndroidFindBy(accessibility = "test-Last Name")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeTextField[`name == \"test-Last Name\"`]")
    private WebElement lastNameTextField;

    @AndroidFindBy(accessibility = "test-Zip/Postal Code")
    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeTextField[`name == \"test-Zip/Postal Code\"`]")
    private WebElement zipcodeTextField;

    @AndroidFindBy(accessibility = "test-CONTINUE")
    @iOSXCUITFindBy(accessibility = "test-CONTINUE")
    private WebElement continueButton;

    public CheckoutInfoPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void validateNavigationToCheckoutInfoPage() {
        try {
            validateElementDisplayed(checkoutInfoBanner,
                    "Failed to navigate to Checkout: Information Page. Expected banner not displayed.");
            ReportsManager.passStep("Successfully navigated to Checkout: Information Page.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to navigate to Checkout: Information Page. Reason: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Checkout: Information Page. Reason: " + e.getMessage());
        }
    }

    public void enterCustomerDetails(User userDetails) {
        try {
            verifyElementDisplayed(firstNameTextField, "First Name field not displayed.");
            enterText(firstNameTextField, userDetails.getFirstName());
            verifyElementDisplayed(lastNameTextField, "Last Name field not displayed.");
            enterText(lastNameTextField, userDetails.getLastName());
            verifyElementDisplayed(zipcodeTextField, "Zip Code Name field not displayed.");
            enterText(zipcodeTextField, userDetails.getZipCode());
            ReportsManager.passStep("Customer details entered successfully: " + userDetails);
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to enter customer details. Reason: " + e.getMessage());
            throw new RuntimeException("Failed to enter customer details. Reason: " + e.getMessage());
        }
    }

    public void clickContinueButton() {
        try {
            verifyElementClickable(continueButton, "Continue button is not clickable or not displayed.");
            tap(continueButton);
            ReportsManager.passStep("Continue button clicked successfully.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to click Continue button. Reason: " + e.getMessage());
            throw new RuntimeException("Failed to click Continue button. Reason: " + e.getMessage());
        }
    }
}
