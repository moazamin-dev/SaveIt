package com.saveit.model;

import com.saveit.dao.CategoryDAO;

import java.time.LocalDate;

/**
 * @brief Represents an individual expense record within the system.
 *
 * This class stores details about a specific financial expenditure, including
 * the amount, date, and the category it belongs to, linked to a specific user.
 */
public class Expense{

    /** @var int id The unique identifier for the expense record */
    private int id;

    /** @var int user_id The ID of the user who owns this expense */
    private int user_id;

    /** @var double amount The monetary value of the expense */
    private double amount;

    /** @var LocalDate date The date when the expense occurred */
    private LocalDate date;

    /** @var Category category The Category object associated with this expense */
    private Category category;

    /**
     * @brief Constructor that initializes an expense for a specific user.
     * @param user_id The ID of the user to whom this expense belongs.
     */
    public Expense(int user_id){
        this.user_id = user_id;
    }

    /**
     * @brief Gets the unique ID of the expense.
     * @return int The expense ID.
     */
    public int getId() {
        return id;
    }

    /**
     * @brief Sets the unique ID of the expense.
     * @param id The expense ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @brief Gets the monetary amount of the expense.
     * @return double The expense amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @brief Sets the monetary amount of the expense.
     * @param amount The amount to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @brief Sets the user ID associated with this expense.
     * @param user_id The user ID to set.
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * @brief Gets the user ID associated with this expense.
     * @return int The user ID.
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * @brief Gets the Category object assigned to this expense.
     * @return Category The category object.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @brief Gets the ID of the category assigned to this expense.
     * @return int The category ID.
     */
    public int getCategoryID(){
        return category.getCategoryID();
    }

    /**
     * @brief Gets the name of the category assigned to this expense.
     * @return String The category name.
     */
    public String getCategoryName(){return category.getName();}

    /**
     * @brief Resolves and sets the category for the expense based on a name string.
     *
     * Uses the CategoryDAO to look up the existing category for the user and assigns
     * the resulting Category object to this expense.
     *
     * @param c The name of the category to resolve.
     */
    public void setCategory(String c) {
        CategoryDAO categoryDAO = new CategoryDAO();
        this.category = categoryDAO.resolveName(this.user_id, c);
    }

    /**
     * @brief Gets the date of the expense.
     * @return LocalDate The date of the record.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @brief Sets the date of the expense.
     * @param date The LocalDate to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}