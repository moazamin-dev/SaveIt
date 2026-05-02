package com.saveit.controller;

public enum ViewType {
    LOGIN("/com/saveit/view/login-view.fxml", "SaveIt - Login"),
    REGISTER("/com/saveit/view/signup-view.fxml", "SaveIt - Create Account"),
    DASHBOARD("/com/saveit/view/main-view.fxml", "SaveIt - Dashboard"), // Assuming chart is dashboard
    EXPENSE_LOG("/com/saveit/view/expense-log-view.fxml", "SaveIt - History"),
    ADD_EXPENSE("/com/saveit/view/add-expense-view.fxml", "SaveIt - New Expense"),
    BUDGET_SETUP("/com/saveit/view/budget-setup-view.fxml", "SaveIt - Plan Budget");

    private final String fxmlFile;
    private final String title;

    ViewType(String fxmlFile, String title) {
        this.fxmlFile = fxmlFile;
        this.title = title;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }

    public String getTitle() {
        return title;
    }
}
