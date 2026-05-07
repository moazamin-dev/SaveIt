package com.saveit.service;

import com.saveit.model.Category;
import com.saveit.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.saveit.model.Expense;

/**
 * @brief Facade class that orchestrates the various budget-related services.
 *
 * The BudgetManager acts as the central point of contact for the UI layer,
 * delegating specific tasks to specialized services like CycleManager,
 * ExpenseService, and BudgetCalculator. It maintains the overall state of
 * a user's financial tracking session.
 */
public class BudgetManager {

    /** @var User user The user associated with this manager session */
    private final User user;

    /** @var CycleManager cycleManager Handles the logic for budget periods */
    private final CycleManager cycleManager;

    /** @var ExpenseService expenseService Manages storage and retrieval of expenses */
    private final ExpenseService expenseService;

    /** @var BudgetCalculator budgetCalculator Performs mathematical budget computations */
    private final BudgetCalculator budgetCalculator;

    /** @var BudgetNotifier budgetNotifier Monitors and alerts regarding budget limits */
    private final BudgetNotifier budgetNotifier;

    /** @var CategoryService categoryService Manages user-defined spending categories */
    private final CategoryService categoryService;

    /**
     * @brief Constructs a new BudgetManager for a specific user.
     *
     * Initializes all internal services required for full budget management.
     *
     * @param user The User object for whom the budget is being managed.
     */
    public BudgetManager(User user) {
        this.user = user;
        this.cycleManager    = new CycleManager(user);
        this.expenseService  = new ExpenseService(user);
        this.budgetCalculator = new BudgetCalculator(cycleManager, expenseService);
        this.budgetNotifier  = new BudgetNotifier(budgetCalculator, cycleManager, user);
        this.categoryService = new CategoryService(user);
    }

    // --- delegating the original public API verbatim ---

    /**
     * @brief Retrieves the remaining funds in the current cycle.
     * @return double The remaining budget amount.
     */
    public double getRemainingLimit()            { return budgetCalculator.getRemainingLimit(); }

    /**
     * @brief Retrieves the total limit set for the current cycle.
     * @return double The total cycle limit.
     */
    public double getCycleLimit()                { return cycleManager.getCycleLimit(); }

    /**
     * @brief Gets the list of expenses relevant to the current cycle.
     * @return List<Expense> The list of cycle-specific expenses.
     */
    public List<Expense> getExpenseList()        { return expenseService.getExpenseList(); }

    /**
     * @brief Deletes an expense and recalculates the remaining budget.
     * @param e The Expense object to be removed.
     */
    public void deleteExpense(Expense e)         {
        expenseService.deleteExpense(e);
        if (cycleManager.getCycle() != null) {
            budgetCalculator.resetRemaining(cycleManager.getCycleLimit() - budgetCalculator.sum_of_transactions(expenseService.getExpenseList()));
        }
    }

    /**
     * @brief Calculates how much the user can spend per day for the rest of the cycle.
     * @return double The suggested daily limit.
     */
    public double calculateDailyLimit()          { return budgetCalculator.calculateDailyLimit(); }

    /**
     * @brief Sums the amounts of a given list of expenses.
     * @param list The list of expenses to sum.
     * @return double The total sum.
     */
    public double sum_of_transactions(List<Expense> list) { return budgetCalculator.sum_of_transactions(list); }

    /**
     * @brief Checks if the user has exceeded their assigned budget limit.
     * @return boolean True if spending exceeds the limit, false otherwise.
     */
    public boolean isOverBudget()                { return budgetNotifier.isOverBudget(); }

    /**
     * @brief Provides a breakdown of spending categorized by name.
     * @return Map<String, Double> A map of category names to their total spent amounts.
     */
    public Map<String, Double> getSpendingByCategory() { return expenseService.getSpendingByCategory(); }

    /**
     * @brief Retrieves all historical expenses for the user across all cycles.
     * @return List<Expense> The full list of user expenses.
     */
    public List<Expense> getAllExpenses()        { return expenseService.getAllExpenses(); }

    /**
     * @brief Records a new transaction and updates the budget status.
     *
     * If the transaction date falls within the current active cycle, the amount
     * is deducted from the remaining budget and budget alerts are processed.
     *
     * @param amount The monetary value of the transaction.
     * @param category The name of the category for this expense.
     * @param date The date the expense occurred.
     */
    public void addTransaction(double amount, String category, LocalDate date) {
        expenseService.addTransaction(amount, category, date);
        if (cycleManager.getCycle() != null && cycleManager.getCycle().isActive()) {
            LocalDate start = cycleManager.getCycleStartDate();
            LocalDate end = cycleManager.getCycleEndDate();
            if (!date.isBefore(start) && !date.isAfter(end)) {
                budgetCalculator.deduct(amount);
                budgetNotifier.isOverBudget();
            } else {
                System.out.println("Expense saved, but excluded from current budget (Date outside cycle).");
            }
        }
    }

    /**
     * @brief Starts a new financial cycle and resets the remaining budget.
     * @param limit The total budget for the new cycle.
     * @param d1 The start date.
     * @param d2 The end date.
     */
    public void startCycle(double limit, LocalDate d1, LocalDate d2) { cycleManager.startCycle(limit, d1, d2); budgetCalculator.resetRemaining(limit); }

    /**
     * @brief Cancels the current active cycle.
     */
    public void cancelCycle()            { cycleManager.cancelCycle(); }

    /**
     * @brief Checks if the current cycle has reached its end date.
     * @return boolean True if the cycle is finished, false otherwise.
     */
    public boolean checkCycleFinish()    { return cycleManager.checkCycleFinish(); }

    /**
     * @brief Gets the start date of the current cycle.
     * @return LocalDate The start date.
     */
    public LocalDate getCycleStartDate() { return cycleManager.getCycleStartDate(); }

    /**
     * @brief Gets the end date of the current cycle.
     * @return LocalDate The end date.
     */
    public LocalDate getCycleEndDate()   { return cycleManager.getCycleEndDate(); }

    /**
     * @brief Adds a new custom category for the user.
     * @param name The name of the category to add.
     */
    public void addCategory(String name) {
        categoryService.addCategory(name, user.getId());
    }

    /**
     * @brief Removes a category based on its unique identifier.
     * @param categoryId The ID of the category to remove.
     */
    public void removeCategory(int categoryId) {
        categoryService.removeCategory(categoryId);
    }

    /**
     * @brief Retrieves all categories defined for the current user.
     * @return List<Category> A list of the user's categories.
     */
    public List<Category> getCategories() {
        return categoryService.getCategoriesForUser(user.getId());
    }

    /**
     * @brief Resolves a Category object by its ID.
     * @param categoryId The ID to look up.
     * @return Category The matching Category object.
     */
    public Category getCategoryById(int categoryId) {
        return categoryService.resolveById(user.getId(), categoryId);
    }

    /**
     * @brief Resolves a Category object by its name.
     * @param name The name to look up.
     * @return Category The matching Category object.
     */
    public Category getCategoryByName(String name) {
        return categoryService.resolveByName(user.getId(), name);
    }
}