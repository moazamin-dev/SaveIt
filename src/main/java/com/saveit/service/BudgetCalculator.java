package com.saveit.service;

import com.saveit.model.Expense;
import java.time.LocalDate;
import java.util.List;

public class BudgetCalculator {

    private final CycleManager cycleManager;
    private final ExpenseService expenseService;
    private double dailyLimit;
    private double Remaining;

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

    public double getRemainingLimit() { return Remaining; }

    public void deduct(double amount) { Remaining -= amount; }

    public void resetRemaining(double limit) { Remaining = limit; }

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

    public double sum_of_transactions(List<Expense> list) {
        double sum = 0;
        for (Expense e : list) {
            sum += e.getAmount();
        }
        return sum;
    }
}