package com.saveit.model;

import java.util.Date;

public class Expense {

    private int id;
    private double amount;
    private Date date;
    private Category category;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
