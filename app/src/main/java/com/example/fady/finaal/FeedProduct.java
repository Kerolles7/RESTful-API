package com.example.fady.finaal;

/**
 * Created by Fady on 1/11/2018.
 */

public class FeedProduct {

    String prod_id;
    String name;
    String description;
    String price;
    String wholesale_price;
    String date_add;
    String condition;
    String availble_now;
    String prod_info;

    public FeedProduct(String prod_id, String name, String description, String price, String wholesale_price,
                       String date_add, String condition, String availble_now, String prod_info)
    {
        this.prod_id = prod_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.wholesale_price =wholesale_price;
        this.date_add = date_add;
        this.condition = condition;
        this.availble_now = availble_now;
        this.prod_info = prod_info;
    }

    public String getProd_id() {
        return prod_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getWholesale_price() {
        return wholesale_price;
    }

    public String getDate_add() {
        return date_add;
    }

    public String getCondition() {
        return condition;
    }

    public String getAvailble_now() {
        return availble_now;
    }

    public String getProd_info() {
        return prod_info;
    }
}
