package com.saucedemo.app.pages;

import com.saucedemo.app.base.BasePage;
import com.saucedemo.app.controller.ReportsManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {

    @iOSXCUITFindBy(iOSNsPredicate = "name == 'test-Username'")
    @AndroidFindBy(accessibility = "test-Username")
    private WebElement userNameInput;

    @iOSXCUITFindBy(iOSNsPredicate = "name == 'test-Password'")
    @AndroidFindBy(accessibility = "test-Password")
    private WebElement passwordInput;

    @iOSXCUITFindBy(iOSNsPredicate = "name == 'test-LOGIN'")
    @AndroidFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    public LoginPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void login(String userName, String password) {
        try {
            verifyElementDisplayed(userNameInput, "Username input field is not displayed");
            enterText(userNameInput, userName);
            verifyElementDisplayed(passwordInput, "Password input field is not displayed");
            enterText(passwordInput, password);
            verifyElementClickable(loginButton, "Login button is not clickable");
            tap(loginButton);
            ReportsManager.passStep("Login action completed successfully with username: " + userName);
        } catch (RuntimeException e) {
            ReportsManager.failStep("Login action failed. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
