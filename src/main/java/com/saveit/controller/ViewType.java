package com.saveit.controller;

public enum ViewType {
    LOGIN("/com/saveit/view/login-view.fxml"),
    REGISTER("/com/saveit/view/signup-view.fxml"),
    DASHBOARD("/com/saveit/view/main-view.fxml"), // Assuming chart is dashboard
    EXPENSE_LOG("/com/saveit/view/expense-log-view.fxml"),
    ADD_EXPENSE("/com/saveit/view/add-expense-view.fxml"),
    BUDGET_SETUP("/com/saveit/view/budget-setup-view.fxml"),
    REPORT("/com/saveit/view/report-view.fxml");

    private final String fxmlFile;

    ViewType(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }

}
