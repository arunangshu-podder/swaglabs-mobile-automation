package com.saucedemo.app.tests;

import com.saucedemo.app.base.BaseTest;
import com.saucedemo.app.controller.DriverManager;
import com.saucedemo.app.controller.LoggerManager;
import com.saucedemo.app.pages.LoginPage;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test (description = "This is to test the login functionality of the app")
    public void loginTest() {
        LoggerManager.info("Starting test: This is to test the login functionality of the app.");
        try {
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            loginPage.login("standard_user", "secret_sauce");
            LoggerManager.info("Login action performed.");
        } catch (Exception e) {
            LoggerManager.error("Exception in loginTest: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
