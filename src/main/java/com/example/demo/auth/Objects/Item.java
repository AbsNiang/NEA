package com.example.demo.auth.Objects;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.math.BigDecimal;

public class Item {

    private String name;
    private double cost;
    private int quantity;
    private  String tags;
    private String description;
    private String image;

    public Item( String name, double cost, int quantity, String tags, String description,String image) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.tags = tags;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
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
