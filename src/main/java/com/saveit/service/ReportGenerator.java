package com.saveit.service;

import com.saveit.dao.DAO;
import com.saveit.model.Category;
import com.saveit.model.Expense;

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
