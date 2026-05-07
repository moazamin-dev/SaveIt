package com.saveit.service;

import com.saveit.model.Expense;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @brief Service class responsible for generating analytical reports and spending trends.
 *
 * This class processes expense data from the BudgetManager to provide high-level
 * insights such as total spending, daily trends (weekly/monthly), budget usage
 * percentages, and category distributions.
 */
public class ReportGenerator {

    /** @var BudgetManager budgetManager The manager used to fetch transaction and cycle data */
    private final BudgetManager budgetManager;

    /**
     * @brief Constructs a ReportGenerator with a reference to the BudgetManager.
     * @param budgetManager The BudgetManager instance providing the source data.
     */
    public ReportGenerator(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    /**
     * @brief Calculates the total spending for the current active cycle.
     * @return double The sum of all expenses in the current cycle.
     */
    public double getTotalSpending() {
        return budgetManager.getExpenseList().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * @brief Generates a trend of spending over the last 30 days.
     *
     * Groups expenses by their date and sums the amounts for each specific day
     * within the 30-day window.
     *
     * @return Map<LocalDate, Double> A map where keys are dates and values are the total spent on those dates.
     */
    public Map<LocalDate, Double> getMonthlySpendingTrend() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        return budgetManager.getAllExpenses().stream()
                .filter(e -> e.getDate() != null && !e.getDate().isBefore(thirtyDaysAgo))
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    /**
     * @brief Helper method to calculate the total sum of expenses from the 30-day trend.
     * @return double The total spent in the last 30 days.
     */
    private double getTotalLastMonth() {
        return getMonthlySpendingTrend().values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * @brief Calculates the estimated savings based on the current cycle limit.
     *
     * Subtracts the total spending of the last 30 days from the current cycle limit.
     *
     * @return double The difference (savings), or 0 if spending exceeds the limit.
     */
    public double getSavings() {
        double limit = budgetManager.getCycleLimit();
        double spent = getTotalLastMonth();
        return Math.max(0, limit - spent);
    }

    /**
     * @brief Calculates the percentage of the budget used in the last 30 days.
     * @return double The percentage (0-100), or 0 if no limit is set.
     */
    public double getBudgetUsagePercentage() {
        double limit = budgetManager.getCycleLimit();
        if (limit <= 0) return 0;
        return (getTotalLastMonth() / limit) * 100;
    }

    /**
     * @brief Provides a breakdown of spending by category for the current cycle.
     *
     * Groups current cycle expenses by their category name and sums the totals.
     *
     * @return Map<String, Double> A map of category names to total spent amounts.
     */
    public Map<String, Double> getCategoryDistribution() {
        return budgetManager.getExpenseList().stream()
                .collect(Collectors.groupingBy(
                        e -> (e.getCategory() != null) ? e.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    /**
     * @brief Generates a trend of spending over the last 7 days.
     *
     * @return Map<LocalDate, Double> A map where keys are dates and values are the daily spending totals.
     */
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