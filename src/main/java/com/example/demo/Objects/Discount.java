package com.example.demo.Objects;

public class Discount {
    private int percentageOff;
    private double purchaseThreshold; //amount of money needed to be spent for the customer to get the discount

    public Discount(int percentageOff, double purchaseThreshold) {
        this.percentageOff = percentageOff;
        this.purchaseThreshold = purchaseThreshold;
    }

    public int getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(int percentageOff) {
        this.percentageOff = percentageOff;
    }

    public double getPurchaseThreshold() {
        return purchaseThreshold;
    }

    public void setPurchaseThreshold(double purchaseThreshold) {
        this.purchaseThreshold = purchaseThreshold;
    }
}
