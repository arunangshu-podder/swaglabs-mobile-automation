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

public class CheckoutOverviewPage extends BasePage {
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"CHECKOUT: OVERVIEW\")")
    @iOSXCUITFindBy(iOSNsPredicate = "label == 'CHECKOUT: OVERVIEW' AND type == 'XCUIElementTypeOther'")
    private WebElement checkoutOverviewBanner;

    @AndroidFindBy(accessibility = "test-FINISH")
    @iOSXCUITFindBy(accessibility = "test-FINISH")
    private WebElement finishButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().textStartsWith(\"Item total:\")")
    private WebElement itemTotal;

    private String androidProductNameScrollable = "new UiScrollable(new UiSelector()).scrollIntoView(text(\"%s\"))";
    private String androidProductName = "new UiSelector().text(\"%s\")";
    private String iosProductName = "label == '%s' AND type == 'XCUIElementTypeStaticText'";

    private String androidProductPrice = "//android.widget.TextView[@text='%s']//parent::android.view.ViewGroup//following-sibling::android.view.ViewGroup[@content-desc='test-Price']/android.widget.TextView";
    private String iosProductPrice = "//XCUIElementTypeStaticText[@name='%s']//parent::XCUIElementTypeOther//following-sibling::XCUIElementTypeOther[@name='test-Price']/XCUIElementTypeStaticText";

    private String androidFinishButtonScrollable = "new UiScrollable(new UiSelector()).scrollIntoView(description(\"test-FINISH\"))";

    public CheckoutOverviewPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void validateNavigationToCheckoutOverviewPage() {
        try {
            validateElementDisplayed(checkoutOverviewBanner,
                    "Failed to navigate to Checkout: Overview Page. Expected banner not displayed.");
            ReportsManager.passStep("Successfully navigated to Checkout: Overview Page.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to navigate to Checkout: Overview Page. Reason: " + e.getMessage());
            Assert.fail("Failed to navigate to Checkout: Overview Page. Reason: " + e.getMessage());
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
            scrollIntoView(androidScrollableLocator, iosLocator);
            if (DRIVER instanceof AndroidDriver)
                validateElementDisplayed(androidLocator,
                        String.format("Product with name %s not displayed.", product.getProductName()));
            else
                validateElementDisplayed(iosLocator,
                        String.format("Product with name %s not displayed.", product.getProductName()));
            ReportsManager.passStep(String.format("Product '%s' name validated successfully.", product.getProductName()));
        } catch (RuntimeException e) {
            ReportsManager.failStep(String.format("Failed to validate product name for %s. Reason: %s",
                            product.getProductName(), e.getMessage()));
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
            ReportsManager.failStep(String.format("Failed to validate product price for %s. Reason: %s",
                    product.getProductName(), e.getMessage()));
            Assert.fail(String.format("Failed to validate product price for %s. Reason: %s",
                    product.getProductName(), e.getMessage()));
        }
    }

    public void validateItemTotal(Product... products) {
        try {
            // Calculate expected total from the product prices
            double expectedTotal = 0.0;
            for (Product product : products) {
                expectedTotal += product.getProductPrice();
            }
            // Scroll to the item total element
            while (!isElementDisplayed(itemTotal, 2))
                scroll("down");
            // Validate the item total text
            double actualTotal = Double.parseDouble(
                    getElementText(itemTotal).replace("Item total: $", ""));
            if (expectedTotal != actualTotal)
                Assert.fail(String.format(
                        "Item total does not match. Expected: %s. Actual: %s.", expectedTotal, actualTotal));
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to validate item total. Reason: " + e.getMessage());
            Assert.fail("Failed to validate item total. Reason: " + e.getMessage());
        }
    }

    public void clickFinishButton() {
        try {
            By androiFinishButton = AppiumBy.androidUIAutomator(androidFinishButtonScrollable);
            scrollIntoView(androiFinishButton, null);
            verifyElementClickable(finishButton,
                    "Finish button is not displayed or not clickable.");
            tap(finishButton);
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to click Finish button. Reason: " + e.getMessage());
            Assert.fail("Failed to click Finish button. Reason: " + e.getMessage());
        }
    }
}
