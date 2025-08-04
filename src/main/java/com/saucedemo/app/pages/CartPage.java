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

public class CartPage extends BasePage {
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"YOUR CART\")")
    @iOSXCUITFindBy(iOSNsPredicate = "label == 'YOUR CART' AND type == 'XCUIElementTypeOther'")
    private WebElement yourCartBanner;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    @iOSXCUITFindBy(accessibility = "test-CHECKOUT")
    private WebElement checkoutButton;

    private String androidProductNameScrollable = "new UiScrollable(new UiSelector()).scrollIntoView(text(\"%s\"))";
    private String androidProductName = "new UiSelector().text(\"%s\")";
    private String iosProductName = "label == '%s' AND type == 'XCUIElementTypeStaticText'";

    private String androidProductPrice = "//android.widget.TextView[@text='%s']//parent::android.view.ViewGroup//following-sibling::android.view.ViewGroup[@content-desc='test-Price']/android.widget.TextView";
    private String iosProductPrice = "//XCUIElementTypeStaticText[@name='%s']//parent::XCUIElementTypeOther//following-sibling::XCUIElementTypeOther[@name='test-Price']/XCUIElementTypeStaticText";

    private String androidCheckoutButtonScrollable = "new UiScrollable(new UiSelector()).scrollIntoView(description(\"test-CHECKOUT\"))";

    public CartPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void validateNavigationToCartPage() {
        try {
            validateElementDisplayed(yourCartBanner,
                    "Failed to navigate to Your Cart Page. Expected banner not displayed.");
            ReportsManager.passStep("Successfully navigated to Your Cart Page.");
        } catch (RuntimeException e) {
            Assert.fail("Failed to navigate to Your Cart Page. Reason: " + e.getMessage());
        }
    }

    public void validateProductDetails(Product... products) {
        for (Product product : products) {
            validateProductName(product);
            validateProductPrice(product);
        }
    }

    public void validateProductName(Product product) {
        try {
            By androidScrollableLocator = AppiumBy.androidUIAutomator(
                    String.format(androidProductNameScrollable, product.getProductName()));
            By androidLocator = AppiumBy.androidUIAutomator(String.format(androidProductName, product.getProductName()));
            By iosLocator = AppiumBy.xpath(String.format(iosProductName, product.getProductName()));
            //Scroll to element
            if (DRIVER instanceof AndroidDriver) {
                getElement(androidScrollableLocator);
            } else {
                iosScroll(DRIVER,
                        String.format(iosProductName, product.getProductName()), "predicateString");
            }
            //Validate product name
            if (DRIVER instanceof AndroidDriver)
                validateElementDisplayed(androidLocator,
                        String.format("Product with name %s not displayed.", product.getProductName()));
            else
                validateElementDisplayed(iosLocator,
                        String.format("Product with name %s not displayed.", product.getProductName()));
            ReportsManager.passStep(String.format("Product '%s' name validated successfully.", product.getProductName()));
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to validate product name for " + product.getProductName() + ". Reason: " + e.getMessage());
            Assert.fail(String.format("Failed to validate product name for %s. Reason: %s",
                    product.getProductName(), e.getMessage()));
        }
    }

    public void validateProductPrice(Product product) {
        try {
            By androidLocator = AppiumBy.xpath(String.format(androidProductPrice, product.getProductName()));
            By iosLocator = AppiumBy.xpath(String.format(iosProductPrice, product.getProductName()));
            String actualPrice = "";
            if (DRIVER instanceof AndroidDriver) {
                validateElementDisplayed(androidLocator,
                        String.format("Price of product with name %s not displayed.", product.getProductName()));
                actualPrice = getElement(androidLocator).getText();
            } else {
                validateElementDisplayed(iosLocator,
                        String.format("Price of product with name %s not displayed.", product.getProductName()));
                actualPrice = getElement(iosLocator).getText();
            }
            if (!actualPrice.replace("$", "").equals(Double.toString(product.getProductPrice()))) {
                Assert.fail(String.format("Product name: %s. Expected price: $%s. Actual price: %s",
                        product.getProductName(), product.getProductPrice(), actualPrice));
            }
            ReportsManager.passStep(String.format("Product '%s' price validated successfully.", product.getProductName()));
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to validate product price for " + product.getProductName() + ". Reason: " + e.getMessage());
            Assert.fail(String.format("Failed to validate product price for %s. Reason: %s",
                    product.getProductName(), e.getMessage()));
        }
    }

    public void clickCheckoutButton() {
        try {
            scrollIntoView(AppiumBy.androidUIAutomator(androidCheckoutButtonScrollable), null);
            verifyElementClickable(checkoutButton, "Checkout button is not displayed or not clickable.");
            tap(checkoutButton);
            ReportsManager.passStep("Checkout button clicked successfully.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to click on Checkout button. Reason: " + e.getMessage());
            Assert.fail("Failed to click on Checkout button. Reason: " + e.getMessage());
        }
    }


}
