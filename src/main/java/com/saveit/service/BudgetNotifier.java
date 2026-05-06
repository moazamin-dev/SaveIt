package com.saveit.service;

import com.saveit.model.User;

public class BudgetNotifier {

    private final BudgetCalculator budgetCalculator;
    private final CycleManager cycleManager;
    private final User user;

    public BudgetNotifier(BudgetCalculator budgetCalculator, CycleManager cycleManager, User user) {
        this.budgetCalculator = budgetCalculator;
        this.cycleManager = cycleManager;
        this.user = user;
    }

    public boolean isOverBudget() {
        if (cycleManager.getCycle() == null || cycleManager.getCycleLimit() <= 0) {
            return false;
        }

        double limit = cycleManager.getCycleLimit();
        double spent = limit - budgetCalculator.getRemainingLimit();

        NotificationService ns = new NotificationService();

        if (spent > 0) {
            ns.checkBudgetStatus(spent, limit);
        }

        if (budgetCalculator.getRemainingLimit() <= 0) {
            ns.showPopup("Monthly limit Exceeded!");
            return true;
        }

        return false;
    }
}