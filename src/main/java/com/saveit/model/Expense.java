package com.saveit.model;

import com.saveit.dao.CategoryDAO;

import java.time.LocalDate;

public class Expense{

    private int id;
    private int user_id;
    private double amount;
    private LocalDate date;
    private Category category;

    public Expense(int user_id){
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public Category getCategory() {
        return category;
    }

    public int getCategoryID(){
        return category.getCategoryID();
    }
    public String getCategoryName(){return category.getName();}

    public void setCategory(String c) {
        CategoryDAO categoryDAO = new CategoryDAO();
        this.category = categoryDAO.resolveName(this.user_id, c);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }


}