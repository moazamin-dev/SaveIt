package com.saveit.service;

import com.saveit.dao.CategoryDAO;
import com.saveit.model.Category;
import com.saveit.model.User;

import java.util.Collections;
import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final User user;

    public CategoryService(User user) {
        this.categoryDAO = new CategoryDAO();
        this.user = user;
    }

    public void addCategory(String name, int userId) {
        Category category = new Category(name.trim(), userId);
        categoryDAO.save(category);
    }

    public void removeCategory(int categoryId) {
        categoryDAO.delete(categoryId);
    }

    public List<Category> getCategoriesForUser(int userId) {
        List<Category> categories = categoryDAO.getAll(userId);
        return categories != null ? categories : Collections.emptyList();
    }


    public Category resolveById(int userId, int categoryId) {
        Category result = categoryDAO.resolveID(userId, categoryId);
        if (result == null || result.getName() == null) {
            System.err.println("Warning: no category found for id=" + categoryId);
            return null;
        }
        return result;
    }

    public Category resolveByName(int userId, String name) {
        return categoryDAO.resolveName(userId, name.trim());
    }
}