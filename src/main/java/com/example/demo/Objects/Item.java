package com.example.demo.Objects;

public class Item {

    private String name;
    private double cost;
    private int quantity;
    private String tags;
    private String description;

    public Item(String name, double cost, int quantity, String tags, String description) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.tags = tags;
        this.description = description;
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
}
