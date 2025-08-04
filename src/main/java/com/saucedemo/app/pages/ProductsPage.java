package com.saucedemo.app.pages;

import com.saucedemo.app.base.BasePage;
import com.saucedemo.app.controller.ReportsManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends BasePage {
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"PRODUCTS\")")
    @iOSXCUITFindBy(iOSNsPredicate = "label == 'PRODUCTS' AND type == 'XCUIElementTypeOther'")
    private WebElement productsBanner;

    @AndroidFindBy(accessibility = "test-Cart")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Cart']")
    private WebElement cartButton;

    @AndroidFindBy(accessibility = "test-Menu")
    @iOSXCUITFindBy(accessibility = "test-Menu")
    private WebElement menuButton;

    private String androidAddToCartScrollable = "new UiScrollable(new UiSelector()).scrollIntoView(text(\"%s\"))";
    private String iosAddToCartScrollable = "label == '%s'";
    private String androidAddToCartByProductName = "//android.widget.TextView[@text='%s']//following-sibling::android.view.ViewGroup[@content-desc='test-ADD TO CART']";
    private String iosAddToCartByProductName = "//XCUIElementTypeStaticText[@label='%s']//ancestor::XCUIElementTypeOther[contains(@label, '%s')][2]//XCUIElementTypeOther[@name='ADD TO CART']";
    private String androidRemoveByProductName = "//android.widget.TextView[@text='%s']//following-sibling::android.view.ViewGroup[@content-desc='test-REMOVE']";
    private String iosRemoveByProductName = "//XCUIElementTypeStaticText[@label='%s']//ancestor::XCUIElementTypeOther[contains(@label, '%s')][2]//XCUIElementTypeOther[@name='REMOVE']";

    public ProductsPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void validateNavigationToProductsPage() {
        try {
            validateElementDisplayed(productsBanner,
                    "Failed to navigate to Products Page. Expected banner not displayed.");
            ReportsManager.passStep("Successfully navigated to Products Page.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to navigate to Products Page. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addProductToCart(String productName) {
        try {
            scrollToProductAndClick(productName);
            validateRemoveButtonForProduct(productName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void scrollToProductAndClick(String productName) {
        try {
            By androidAddToCartScrollableLocator = AppiumBy.androidUIAutomator(String.format(androidAddToCartScrollable, productName));
            By androidAddToCartLocator = AppiumBy.xpath(String.format(androidAddToCartByProductName, productName));
            By iosAddToCartLocator = AppiumBy.xpath(String.format(iosAddToCartByProductName, productName, productName));
            // Scroll product to view
            if (DRIVER instanceof AndroidDriver) {
                getElement(androidAddToCartScrollableLocator);
            } else {
                iosScroll(DRIVER,
                        String.format(iosAddToCartScrollable, "\uDB81\uDF41 " + productName), "predicateString");
            }
            // Click on Add to Cart button
            if (DRIVER instanceof AndroidDriver) {
                tap(DRIVER.findElement(androidAddToCartLocator));
            } else {
                tap(DRIVER.findElement(iosAddToCartLocator));
            }
            ReportsManager.passStep(
                    String.format("Product '%s' added to cart successfully.", productName));
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to add product to cart. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void validateRemoveButtonForProduct(String productName) {
        try {
            By androidRemoveLocator = AppiumBy.xpath(String.format(androidRemoveByProductName, productName, productName));
            By iosRemoveLocator = AppiumBy.xpath(String.format(iosRemoveByProductName, productName, productName));
            if (DRIVER instanceof AndroidDriver)
                validateElementDisplayed(androidRemoveLocator ,
                        String.format("Remove button for product %s not displayed.", productName));
            else
                validateElementDisplayed(iosRemoveLocator ,
                        String.format("Remove button for product %s not displayed.", productName));
            ReportsManager.passStep(
                    String.format("Remove button for product '%s' is displayed after adding to cart.", productName));
        } catch (RuntimeException e) {
            ReportsManager.failStep("Remove button not displayed for given product. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickCartButton() {
        try {
            verifyElementClickable(cartButton,
                    "Cart button is not displayed or clickable.", 2);
            addDelay(5);
            click(cartButton);
            ReportsManager.passStep("Cart button clicked successfully.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to click on Cart button. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickMenuButton() {
        try {
            verifyElementClickable(menuButton,
                    "Menu button is not displayed or clickable.", 2);
            tap(menuButton);
            ReportsManager.passStep("Menu button clicked successfully.");
        } catch (RuntimeException e) {
            ReportsManager.failStep("Failed to click on Menu button. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
