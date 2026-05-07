package com.saveit.service;

import com.saveit.dao.CategoryDAO;
import com.saveit.model.Category;
import com.saveit.model.User;

import java.util.Collections;
import java.util.List;

/**
 * @brief Service class providing business logic for managing categories.
 *
 * This class acts as an intermediary between the UI/Controller layers and the CategoryDAO.
 * It handles the creation, deletion, and resolution of categories specifically for
 * an authenticated user.
 */
public class CategoryService {

    /** @var CategoryDAO categoryDAO The data access object for database interactions */
    private final CategoryDAO categoryDAO;

    /** @var User user The user associated with this service instance */
    private final User user;

    /**
     * @brief Constructs a CategoryService for a specific user.
     *
     * @param user The User object who owns the categories being managed.
     */
    public CategoryService(User user) {
        this.categoryDAO = new CategoryDAO();
        this.user = user;
    }

    /**
     * @brief Creates and saves a new category for a user.
     *
     * @param name The name of the category (will be trimmed of whitespace).
     * @param userId The ID of the user creating the category.
     */
    public void addCategory(String name, int userId) {
        Category category = new Category(name.trim(), userId);
        categoryDAO.save(category);
    }

    /**
     * @brief Removes a category from the system based on its ID.
     *
     * @param categoryId The unique identifier of the category to delete.
     */
    public void removeCategory(int categoryId) {
        categoryDAO.delete(categoryId);
    }

    /**
     * @brief Retrieves all categories belonging to a specific user.
     *
     * @param userId The ID of the user whose categories are being requested.
     * @return List<Category> A list of categories, or an empty list if none are found.
     */
    public List<Category> getCategoriesForUser(int userId) {
        List<Category> categories = categoryDAO.getAll(userId);
        return categories != null ? categories : Collections.emptyList();
    }

    /**
     * @brief Resolves a Category object by its unique ID and verifies its existence.
     *
     * @param userId The ID of the user.
     * @param categoryId The ID of the category to resolve.
     * @return Category The found Category object, or null if no valid category exists.
     */
    public Category resolveById(int userId, int categoryId) {
        Category result = categoryDAO.resolveID(userId, categoryId);
        if (result == null || result.getName() == null) {
            System.err.println("Warning: no category found for id=" + categoryId);
            return null;
        }
        return result;
    }

    /**
     * @brief Resolves a Category object by its name.
     *
     * If the category does not exist, the underlying DAO implementation may create it.
     *
     * @param userId The ID of the user.
     * @param name The name of the category (will be trimmed of whitespace).
     * @return Category The resolved Category object.
     */
    public Category resolveByName(int userId, String name) {
        return categoryDAO.resolveName(userId, name.trim());
    }
}