package com.saveit.model;

/**
 * @brief Represents a category used to classify financial entries or items.
 *
 * This model class holds information about a specific category, including
 * its unique identifier, name, and the user it belongs to.
 */
public class Category {

    /** @var int categoryID The unique identifier for the category */
    private int categoryID;

    /** @var String name The display name of the category */
    private String name;

    /** @var int user_id The ID of the user who owns this category */
    private int user_id;

    /**
     * @brief Default constructor for the Category class.
     */
    public Category() {}

    /**
     * @brief Parameterized constructor to create a new Category.
     *
     * @param name The name to assign to the category.
     * @param user_id The ID of the user associated with this category.
     */
    public Category(String name, int user_id) {
        this.name = name;
        this.user_id = user_id;
    }

    /**
     * @brief Gets the name of the category.
     * @return String The category name.
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Sets the name of the category.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Gets the unique ID of the category.
     * @return int The category ID.
     */
    public int getCategoryID() { return categoryID; }

    /**
     * @brief Sets the unique ID of the category.
     * @param categoryID The unique ID to set.
     */
    public void setCategoryID(int categoryID){ this.categoryID = categoryID;}

    /**
     * @brief Gets the ID of the user associated with this category.
     * @return int The user ID.
     */
    public int getUser_id() { return user_id; }

    /**
     * @brief Sets the ID of the user associated with this category.
     * @param user_id The user ID to set.
     */
    public void setUser_id(int user_id) {this.user_id = user_id;}
}