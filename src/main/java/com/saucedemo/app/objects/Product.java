package com.saucedemo.app.objects;

public class Product {
    private String productName;
    private double productPrice;

    public Product(String productName, double productPrice) {
        setProductName(productName);
        setProductPrice(productPrice);
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
