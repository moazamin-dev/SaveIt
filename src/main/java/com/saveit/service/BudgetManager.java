package com.saveit.service;

import com.saveit.dao.CycleDAO;
import com.saveit.model.Category;
import com.saveit.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.saveit.model.Expense;

public class BudgetManager {

    private final User user;
    private final CycleManager cycleManager;
    private final ExpenseService expenseService;
    private final BudgetCalculator budgetCalculator;
    private final BudgetNotifier budgetNotifier;
    private final CategoryService categoryService;

    public BudgetManager(User user) {
        this.user = user;
        this.cycleManager    = new CycleManager(user);
        this.expenseService  = new ExpenseService(user);
        this.budgetCalculator = new BudgetCalculator(cycleManager, expenseService);
        this.budgetNotifier  = new BudgetNotifier(budgetCalculator, cycleManager, user);
        this.categoryService = new CategoryService(user);
    }

    // --- delegating the original public API verbatim ---

    public double getRemainingLimit()            { return budgetCalculator.getRemainingLimit(); }
    public double getCycleLimit()                { return cycleManager.getCycleLimit(); }
    public List<Expense> getExpenseList()        { return expenseService.getExpenseList(); }
    public void deleteExpense(Expense e)         {
        expenseService.deleteExpense(e);
        if (cycleManager.getCycle() != null) {
            budgetCalculator.resetRemaining(cycleManager.getCycleLimit() - budgetCalculator.sum_of_transactions(expenseService.getExpenseList()));
        }
    }
    public double calculateDailyLimit()          { return budgetCalculator.calculateDailyLimit(); }
    public double sum_of_transactions(List<Expense> list) { return budgetCalculator.sum_of_transactions(list); }
    public boolean isOverBudget()                { return budgetNotifier.isOverBudget(); }
    public Map<String, Double> getSpendingByCategory() { return expenseService.getSpendingByCategory(); }
    public List<Expense> getAllExpenses()        { return expenseService.getAllExpenses(); }

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

    public void startCycle(double limit, LocalDate d1, LocalDate d2) { cycleManager.startCycle(limit, d1, d2); budgetCalculator.resetRemaining(limit); }
    public void cancelCycle()            { cycleManager.cancelCycle(); }
    public boolean checkCycleFinish()    { return cycleManager.checkCycleFinish(); }
    public LocalDate getCycleStartDate() { return cycleManager.getCycleStartDate(); }
    public LocalDate getCycleEndDate()   { return cycleManager.getCycleEndDate(); }

    public void addCategory(String name) {
        categoryService.addCategory(name, user.getId());
    }
    public void removeCategory(int categoryId) {
        categoryService.removeCategory(categoryId);
    }
    public List<Category> getCategories() {
        return categoryService.getCategoriesForUser(user.getId());
    }
    public Category getCategoryById(int categoryId) {
        return categoryService.resolveById(user.getId(), categoryId);
    }
    public Category getCategoryByName(String name) {
        return categoryService.resolveByName(user.getId(), name);
    }
}