package com.example.demo.Objects;

import java.time.LocalDate;

public class Transaction {//format date into access form dd/mm/yyyy, time into hh:mm:ss
    private String emailAddress;
    private double moneySpent;
    private LocalDate dateOfTransaction;
    private String timeOfTransaction;

    public Transaction(String emailAddress, double moneySpent, LocalDate dateOfTransaction, String timeOfTransaction) {
        this.emailAddress = emailAddress;
        this.moneySpent = moneySpent;
        this.dateOfTransaction = dateOfTransaction;
        this.timeOfTransaction = timeOfTransaction;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(String timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }
}
