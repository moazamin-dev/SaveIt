package com.saveit.model;

import com.saveit.dao.CategoryDAO;

public class Category {

    private int categoryID;
    private String name;
    private int user_id;

    public Category() {}

    public Category(String name, int user_id) {
        this.name = name;
        this.user_id = user_id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryID() { return categoryID; }

    public void setCategoryID(int categoryID){ this.categoryID = categoryID;}

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) {this.user_id = user_id;}
}