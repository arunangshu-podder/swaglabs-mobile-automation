package com.saucedemo.app.tests;

import com.saucedemo.app.base.BaseTest;
import com.saucedemo.app.controller.DriverManager;
import com.saucedemo.app.pages.ProductsPage;
import org.testng.annotations.Test;

public class MiscellaneousTests extends BaseTest {

    @Test
    public void validateWebViewFuntionality() {
        ProductsPage productsPage = new ProductsPage(DriverManager.getDriver());
        productsPage.clickMenuButton();
    }
}
