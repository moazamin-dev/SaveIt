package com.budget.service;

import com.budget.dao.DAO;
import com.budget.dao.CycleDAO;
import com.budget.model.Category;
import com.budget.model.Expense;
import com.budget.model.User;

import java.util.Date;
import java.util.Map;

public class BudgetManager {

    private User user;
    private DAO<Expense> expenseDAO;
    private double monthlyLimit;
    private Date startDate;
    private Date endDate;

    public double calculateDailyLimit() {
        // TODO: implement
        return 0;
    }

    public double calculateRemainingBalance() {
        // TODO: implement
        return 0;
    }

    public boolean checkCycleFinish() {
        // TODO: implement
        return false;
    }

    public void cancelCycle() {
        // TODO: implement
    }

    public Map<Category, Double> getSpendingByCategory() {
        // TODO: implement
        return null;
    }

    public boolean isOverBudget() {
        // TODO: implement
        return false;
    }

    public void addTransaction(Expense e) {
        // TODO: implement
    }

    public void startCycle(double limit, Date d1, Date d2) {
        // TODO: implement
    }
}
