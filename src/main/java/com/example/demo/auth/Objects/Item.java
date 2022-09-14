package com.example.demo.auth.Objects;

import java.math.BigDecimal;

public class Item {

    private int itemID;
    private String name;
    private BigDecimal cost;
    private int quantity;
    private  String tags;
    private String description;
    private String image;

    public Item(int itemID, String name, BigDecimal cost, int quantity, String tags, String description,String image) {
        this.itemID = itemID;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.tags = tags;
        this.description = description;
        this.image = image;
    }

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getImage(){
        return image;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
