package com.saveit.dao;

import com.saveit.model.Category;
import java.sql.Connection;
import java.util.List;

public class CategoryDAO implements DAO<Category> {

    private Connection connection;

    @Override
    public void save(Category c) {
    }

    @Override
    public List<Category> getAll() {
        // TODO: implement
        return null;
    }

    @Override
    public void delete(int id) {
        // TODO: implement
    }

    public List<Category> getCategoryList(Category category) {
        // TODO: implement
        return null;
    }
}
