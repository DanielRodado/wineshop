package com.wineshop.ecommerce.utils;

public final class ProductUtil {

    public static Double calculatePriceOrder(int amount, Double price) {
        return amount * price;
    }

}
