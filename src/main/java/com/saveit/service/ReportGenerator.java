package com.budget.service;

import com.budget.dao.DAO;
import com.budget.model.Category;
import com.budget.model.Expense;

import java.time.LocalDate;
import java.util.Map;

public class ReportGenerator {

    private DAO<Expense> expenseDAO;

    public Map<Category, Double> getCategoryDistribution() {
        // TODO: implement
        return null;
    }

    public Map<LocalDate, Double> getWeeklySpendingTrend() {
        // TODO: implement
        return null;
    }

    public Map<LocalDate, Double> getMonthlySpendingTrend() {
        // TODO: implement
        return null;
    }

    public void generatePDFReport() {
        // TODO: implement
    }
}
