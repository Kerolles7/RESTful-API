package com.example.fady.finaal;

/**
 * Created by Fady on 1/10/2018.
 */

public class FeedCategory {

    public String name;
    public String price;
    public String id_product;
    public String id_image;


    public FeedCategory(String name,String price,String id_product,String id_image)
    {
        this.name = name;
        this.price = price;
        this.id_product = id_product;
        this.id_image = id_image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getId_product() {
        return id_product;
    }

    public String getId_image() {
        return id_image;
    }
}
