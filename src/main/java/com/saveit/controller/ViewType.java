package com.saveit.controller;

/**
 * @brief Enumeration defining the available views and their corresponding FXML file paths.
 *
 * This enum serves as a central registry for navigation within the application.
 * Each constant maps a logical view name (e.g., DASHBOARD) to the physical
 * resource location of its JavaFX FXML layout file.
 */
public enum ViewType {

    /** @brief The login screen view */
    LOGIN("/com/saveit/view/login-view.fxml"),

    /** @brief The user registration/signup screen view */
    REGISTER("/com/saveit/view/signup-view.fxml"),

    /** @brief The main dashboard view, typically displaying charts and summaries */
    DASHBOARD("/com/saveit/view/main-view.fxml"),

    /** @brief The view displaying the historical log of all expenses */
    EXPENSE_LOG("/com/saveit/view/expense-log-view.fxml"),

    /** @brief The form view for adding a new transaction */
    ADD_EXPENSE("/com/saveit/view/add-expense-view.fxml"),

    /** @brief The initial setup view for creating a new budget cycle */
    BUDGET_SETUP("/com/saveit/view/budget-setup-view.fxml"),

    /** @brief The detailed reporting and analytics view */
    REPORT("/com/saveit/view/report-view.fxml");

    /** @var String fxmlFile The internal resource path to the FXML file */
    private final String fxmlFile;

    /**
     * @brief Internal constructor for the ViewType enum.
     * @param fxmlFile The path to the FXML resource.
     */
    ViewType(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    /**
     * @brief Retrieves the FXML file path associated with the view.
     * @return String The resource path to the FXML.
     */
    public String getFxmlFile() {
        return fxmlFile;
    }

}