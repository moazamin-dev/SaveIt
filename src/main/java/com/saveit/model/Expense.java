package com.saveit.model;

import java.time.LocalDate;

public class Expense{

    private int id;
    private int user_id;
    private double amount;
    private LocalDate date;
    private Category category;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUser_Id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public Category getCategory() {
        return category;
    }

    public int getCategoryID(){
        return category.getId();
    }
    public String getCategoryName(){return category.getName();}

    public void setCategory(String c) {
        category = new Category(c);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public int getId() {
        return id;
    }
}