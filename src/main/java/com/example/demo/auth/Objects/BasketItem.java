package com.example.demo.auth.Objects;

public class BasketItem {
    private String ItemName;
    private int quantityAdded;

    public BasketItem(String itemName, int quantityAdded) {
        ItemName = itemName;
        this.quantityAdded = quantityAdded;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    public void setQuantityAdded(int quantityAdded) {
        this.quantityAdded = quantityAdded;
    }
}
