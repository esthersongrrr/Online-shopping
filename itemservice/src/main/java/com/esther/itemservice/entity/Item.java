package com.esther.itemservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Item {
    @Id
    private String id;
    private String name;
    private double price;
    private String description;
    private List<String> pictureUrls;
    private String upc;
    private int inventoryCount;

    public Item() {}

    public Item(String id, String name, double price, String description, List<String> pictureUrls, String upc, int inventoryCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.pictureUrls = pictureUrls;
        this.upc = upc;
        this.inventoryCount = inventoryCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }
}
