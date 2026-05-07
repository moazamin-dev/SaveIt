package com.saveit.service;

import com.saveit.model.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    private final BudgetManager budgetManager;

    public ReportGenerator(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public double getTotalSpending() {
        // Gets expenses for the CURRENT active cycle
        return budgetManager.getExpenseList().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Map<LocalDate, Double> getMonthlySpendingTrend() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        return budgetManager.getAllExpenses().stream()
                .filter(e -> e.getDate() != null && !e.getDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    private double getTotalLastMonth() {
        return getMonthlySpendingTrend().values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double getSavings() {
        double limit = budgetManager.getCycleLimit();
        double spent = getTotalLastMonth();
        return Math.max(0, limit - spent);
    }

    public double getBudgetUsagePercentage() {
        double limit = budgetManager.getCycleLimit();
        if (limit <= 0) return 0;
        return (getTotalLastMonth() / limit) * 100;
    }

    public Map<String, Double> getCategoryDistribution() {
        return budgetManager.getExpenseList().stream()
                .collect(Collectors.groupingBy(
                        e -> (e.getCategory() != null) ? e.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public Map<LocalDate, Double> getWeeklySpendingTrend() {
        LocalDate limitDate = LocalDate.now().minusDays(7);
        return budgetManager.getAllExpenses().stream()
                .filter(e -> e.getDate() != null && !e.getDate().isBefore(limitDate))
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }
}