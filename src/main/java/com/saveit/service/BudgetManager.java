package com.saveit.service;

import com.saveit.dao.DAO;
import com.saveit.dao.CycleDAO;
import com.saveit.model.Category;
import com.saveit.model.Expense;
import com.saveit.model.User;

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
