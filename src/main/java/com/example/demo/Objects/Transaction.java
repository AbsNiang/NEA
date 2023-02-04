package com.example.demo.Objects;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {//format LocalDate into access form dd/mm/yyyy, LocalTime into hh:mm:ss
    private String emailAddress;
    private double moneySpent;
    private LocalDate dateOfTransaction;
    private LocalTime timeOfTransaction;

    public Transaction(String emailAddress, double moneySpent, LocalDate dateOfTransaction, LocalTime timeOfTransaction) {
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

    public LocalTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(LocalTime timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }
}
