package com.saveit.service;

import com.saveit.model.Expense;
import java.time.LocalDate;
import java.util.List;

/**
 * @brief Service class responsible for calculating and managing budget-related metrics.
 *
 * This class computes the remaining balance for a financial cycle and determines
 * the recommended daily spending limit based on the time remaining until the cycle ends.
 */
public class BudgetCalculator {

    /** @var CycleManager cycleManager Reference to the manager handling the current cycle's state */
    private final CycleManager cycleManager;

    /** @var ExpenseService expenseService Reference to the service handling expense records */
    private final ExpenseService expenseService;

    /** @var double dailyLimit The calculated amount the user can spend per day */
    private double dailyLimit;

    /** @var double Remaining The current remaining budget for the cycle */
    private double Remaining;

    /**
     * @brief Constructor for BudgetCalculator.
     *
     * Initializes the calculator by assessing the current cycle's status. If an active
     * cycle exists, it calculates the remaining budget by subtracting the sum of
     * existing transactions from the cycle limit.
     *
     * @param cycleManager The manager providing cycle dates and limits.
     * @param expenseService The service providing the list of expenses to sum.
     */
    public BudgetCalculator(CycleManager cycleManager, ExpenseService expenseService) {
        this.cycleManager   = cycleManager;
        this.expenseService = expenseService;

        if (cycleManager.getCycle() != null && cycleManager.getCycle().isActive()) {
            double spent = sum_of_transactions(expenseService.getExpenseList());
            this.Remaining = cycleManager.getCycleLimit() - spent;
        } else {
            this.Remaining = 0;
        }
    }

    /**
     * @brief Gets the current remaining limit.
     * @return double The remaining budget value.
     */
    public double getRemainingLimit() { return Remaining; }

    /**
     * @brief Manually deducts an amount from the current remaining balance.
     * @param amount The value to subtract from the budget.
     */
    public void deduct(double amount) { Remaining -= amount; }

    /**
     * @brief Resets the remaining balance to a specific value.
     * @param limit The new budget limit to set.
     */
    public void resetRemaining(double limit) { Remaining = limit; }

    /**
     * @brief Calculates the suggested daily spending limit.
     *
     * Determines the number of days remaining until the cycle's end date (inclusive of today)
     * and divides the remaining budget by that count.
     *
     * @return double The calculated daily limit, or 0.0 if no cycle or end date is defined.
     */
    public double calculateDailyLimit() {
        LocalDate endDate = cycleManager.getCycleEndDate();

        if (endDate == null || cycleManager.getCycle() == null) {
            return 0.0;
        }

        LocalDate today = LocalDate.now();

        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, endDate) + 1;

        if (daysLeft <= 0) {
            return getRemainingLimit();
        }

        this.dailyLimit = getRemainingLimit() / daysLeft;
        return this.dailyLimit;
    }

    /**
     * @brief Helper method to calculate the total sum of a list of expenses.
     *
     * @param list The list of Expense objects to iterate through.
     * @return double The total monetary sum of all expenses in the list.
     */
    public double sum_of_transactions(List<Expense> list) {
        double sum = 0;
        for (Expense e : list) {
            sum += e.getAmount();
        }
        return sum;
    }
}