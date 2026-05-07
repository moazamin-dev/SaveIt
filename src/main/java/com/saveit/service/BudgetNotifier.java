package com.saveit.service;

import com.saveit.model.User;

/**
 * @brief Service responsible for monitoring budget status and triggering notifications.
 *
 * This class evaluates the current spending against the set limits of a financial cycle
 * and utilizes the NotificationService to alert the user when thresholds are met or exceeded.
 */
public class BudgetNotifier {

    /** @var BudgetCalculator budgetCalculator Reference to the calculator providing remaining balance data */
    private final BudgetCalculator budgetCalculator;

    /** @var CycleManager cycleManager Reference to the manager providing cycle limit and state data */
    private final CycleManager cycleManager;

    /** @var User user The user associated with these notifications */
    private final User user;

    /**
     * @brief Constructs a BudgetNotifier with necessary dependencies.
     *
     * @param budgetCalculator The calculator used to fetch remaining budget.
     * @param cycleManager The manager used to fetch cycle limits.
     * @param user The User object being monitored.
     */
    public BudgetNotifier(BudgetCalculator budgetCalculator, CycleManager cycleManager, User user) {
        this.budgetCalculator = budgetCalculator;
        this.cycleManager = cycleManager;
        this.user = user;
    }

    /**
     * @brief Checks if the user has exceeded their budget and triggers notifications.
     *
     * This method calculates total spent amount, checks for percentage-based milestones
     * (via NotificationService), and displays a popup alert if the remaining limit
     * reaches zero or less.
     *
     * @return boolean True if the budget limit has been exceeded, false otherwise.
     */
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