package com.saveit.service;

public class BudgetNotifier {

    private final BudgetCalculator budgetCalculator;
    private final CycleManager cycleManager;

    public BudgetNotifier(BudgetCalculator budgetCalculator, CycleManager cycleManager) {
        this.budgetCalculator = budgetCalculator;
        this.cycleManager     = cycleManager;
    }

    public boolean isOverBudget() {
        if (cycleManager.getCycle() == null) { return false; }
        if (budgetCalculator.getRemainingLimit() <= 0) {
            NotificationService NS = new NotificationService();
            NS.checkBudgetStatus(
                    budgetCalculator.sum_of_transactions(
                            new ExpenseService(null).getExpenseList()),   // or inject ExpenseService
                    cycleManager.getCycleLimit());
            NS.showPopup("Monthly limit Exceeded!");
            System.out.println("OverBudget");
            return true;
        }
        return false;
    }
}