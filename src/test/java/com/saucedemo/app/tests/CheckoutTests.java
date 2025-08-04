package com.saucedemo.app.tests;

import com.saucedemo.app.base.BaseTest;
import com.saucedemo.app.controller.DriverManager;
import com.saucedemo.app.controller.LoggerManager;
import com.saucedemo.app.objects.User;
import com.saucedemo.app.pages.*;
import com.saucedemo.app.objects.Product;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {
    @Test(description = "Validate that user is able to complete the checkout process successfully with a single product.",
          groups = {"Dev"})
    public void validateCheckoutProcessWithSingleProduct() {
        try {
            Product product = new Product("Sauce Labs Onesie", 7.99);
            User user = new User("Arunangshu", "Podder", "560068");
            LoggerManager.info("Data setup completed.");

            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            loginPage.login("sttandard_user", "secret_sauce");
            LoggerManager.info("Login action completed.");

            ProductsPage productsPage = new ProductsPage(DriverManager.getDriver());
            productsPage.validateNavigationToProductsPage();
            LoggerManager.info("Navigated to Products page.");

            productsPage.addProductToCart(product.getProductName());
            LoggerManager.info(String.format("Product %s selected", product.getProductName()));

            productsPage.clickCartButton();

            CartPage cartPage = new CartPage(DriverManager.getDriver());
            cartPage.validateNavigationToCartPage();
            LoggerManager.info("Navigated to Your Cart page.");

            cartPage.validateProductDetails(product);
            LoggerManager.info("Product details in Your Cart page validated.");

            cartPage.clickCheckoutButton();

            /*CheckoutInfoPage checkoutInfoPage = new CheckoutInfoPage(DriverManager.getDriver());
            checkoutInfoPage.validateNavigationToCheckoutInfoPage();
            LoggerManager.info("Navigated to Checkout:Information page.");

            checkoutInfoPage.enterCustomerDetails(user);
            LoggerManager.info("Entered user details in Checkout:Information page.");

            checkoutInfoPage.clickContinueButton();

            CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage(DriverManager.getDriver());
            checkoutOverviewPage.validateNavigationToCheckoutOverviewPage();
            LoggerManager.info("Navigated to Checkout:Overview page.");

            checkoutOverviewPage.validateProductDetails(product);
            checkoutOverviewPage.validateItemTotal(product);
            LoggerManager.info("Product details in Checkout:Overview page validated.");

            checkoutOverviewPage.clickFinishButton();

            OrderConfirmationPage orderConfirmationPage = new OrderConfirmationPage(DriverManager.getDriver());
            orderConfirmationPage.validateNavigationToOrderConfirmationPage();
            orderConfirmationPage.validateThankYouMessageDisplayed();
            LoggerManager.info("Navigated to Checkout:Complete page.");*/

        } catch (Exception e) {
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Test failed. Reason: ").append(e.getMessage()).append("\n");
            for (StackTraceElement ste : e.getStackTrace()) {
                errorMsg.append("\tat ").append(ste.toString()).append("\n");
            }
            Assert.fail(errorMsg.toString());
        }
    }

    @Test(description = "Validate that user is able to complete the checkout process successfully with a multiple products.",
            groups = {"Dev1"})
    public void validateCheckoutProcessWithMultipleProducts() {
        try {
            Product product1 = new Product("Sauce Labs Backpack", 29.99);
            Product product2 = new Product("Sauce Labs Onesie", 7.99);
            User user = new User("Arunangshu", "Podder", "560068");
            LoggerManager.info("Data setup completed.");

            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            loginPage.login("standard_user", "secret_sauce");
            LoggerManager.info("Login action completed.");

            ProductsPage productsPage = new ProductsPage(DriverManager.getDriver());
            productsPage.validateNavigationToProductsPage();
            LoggerManager.info("Navigated to Products page.");

            productsPage.addProductToCart(product1.getProductName());
            LoggerManager.info(String.format("Product %s selected", product1.getProductName()));
            productsPage.addProductToCart(product2.getProductName());
            LoggerManager.info(String.format("Product %s selected", product2.getProductName()));

            productsPage.clickCartButton();

            CartPage cartPage = new CartPage(DriverManager.getDriver());
            cartPage.validateNavigationToCartPage();
            LoggerManager.info("Navigated to Your Cart page.");

            cartPage.validateProductDetails(product1, product2);
            LoggerManager.info("Product details in Your Cart page validated.");

            cartPage.clickCheckoutButton();

            CheckoutInfoPage checkoutInfoPage = new CheckoutInfoPage(DriverManager.getDriver());
            checkoutInfoPage.validateNavigationToCheckoutInfoPage();
            LoggerManager.info("Navigated to Checkout:Information page.");

            checkoutInfoPage.enterCustomerDetails(user);
            LoggerManager.info("Entered user details in Checkout:Information page.");

            checkoutInfoPage.clickContinueButton();

            CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage(DriverManager.getDriver());
            checkoutOverviewPage.validateNavigationToCheckoutOverviewPage();
            LoggerManager.info("Navigated to Checkout:Overview page.");

            checkoutOverviewPage.validateProductDetails(product1, product2);
            checkoutOverviewPage.validateItemTotal(product1, product2);
            LoggerManager.info("Product details in Checkout:Overview page validated.");

            checkoutOverviewPage.clickFinishButton();

            OrderConfirmationPage orderConfirmationPage = new OrderConfirmationPage(DriverManager.getDriver());
            orderConfirmationPage.validateNavigationToOrderConfirmationPage();
            orderConfirmationPage.validateThankYouMessageDisplayed();
            LoggerManager.info("Navigated to Checkout:Complete page.");

        } catch (Exception e) {
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Test failed. Reason: ").append(e.getMessage()).append("\n");
            for (StackTraceElement ste : e.getStackTrace()) {
                errorMsg.append("\tat ").append(ste.toString()).append("\n");
            }
            Assert.fail(errorMsg.toString());
        }
    }
}
