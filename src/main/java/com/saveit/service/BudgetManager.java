package com.saveit.service;

import com.saveit.dao.CycleDAO;
import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Cycle;
import com.saveit.model.Expense;
import com.saveit.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BudgetManager {

    // ── Warning threshold constant ────────────────────────────────────────────
    private static final double WARNING_RATIO = 0.80;

    // ── Fields ────────────────────────────────────────────────────────────────

    private final User       user;
    private final ExpenseDAO expenseDAO;
    private       Cycle      cycle;
    private       Expense    e;
    private       double     dailyLimit;
    private       double     Remaining;

    private final List<BudgetListener> listeners = new ArrayList<>();

    // ── Constructor ───────────────────────────────────────────────────────────

    public BudgetManager(User user) {
        this.user = user;
        CycleDAO cycleDAO = new CycleDAO();
        this.cycle = cycleDAO.getCycle(user.getId());
        expenseDAO = new ExpenseDAO();

        if (this.cycle != null && this.cycle.isActive()) {
            double spent = sum_of_transactions(getExpenseList());
            this.Remaining = this.cycle.getLimit() - spent;
        } else {
            this.Remaining = 0;
        }
    }

    // ── Listener registration ─────────────────────────────────────────────────

    public void addBudgetListener(BudgetListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeBudgetListener(BudgetListener listener) {
        listeners.remove(listener);
    }

    // ── Event dispatch ────────────────────────────────────────────────────────

    private void evaluateAndNotify(double spent) {
        if (cycle == null) return;

        double limit = cycle.getLimit();
        double ratio = limit > 0 ? spent / limit : 0;

        BudgetEvent event;
        if (ratio >= 1.0) {
            event = new BudgetEvent(BudgetEvent.Type.BUDGET_EXCEEDED, spent, limit);
        } else if (ratio >= WARNING_RATIO) {
            event = new BudgetEvent(BudgetEvent.Type.BUDGET_WARNING, spent, limit);
        } else {
            event = new BudgetEvent(BudgetEvent.Type.BUDGET_OK, spent, limit);
        }

        fireEvent(event);
    }

    private void fireEvent(BudgetEvent event) {
        for (BudgetListener listener : listeners) {
            listener.onBudgetEvent(event);
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public double getRemainingLimit() { return Remaining; }

    public double getCycleLimit() { return cycle.getLimit(); }

    public List<Expense> getExpenseList() {
        return expenseDAO.getExpenseSinceDate(
                user.getId(), cycle.getStartDate().toString());
    }

    public double calculateDailyLimit() {
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(), cycle.getEndDate());
        if (daysLeft <= 0) return getRemainingLimit();
        dailyLimit = getRemainingLimit() / daysLeft;
        return dailyLimit;
    }

    public double sum_of_transactions(List<Expense> list) {
        double sum = 0;
        for (Expense e : list) { sum += e.getAmount(); }
        return sum;
    }

    public boolean isOverBudget() {
        if (cycle == null) return false;

        double spent = sum_of_transactions(getExpenseList());
        evaluateAndNotify(spent);

        if (Remaining <= 0) {
            System.out.println("OverBudget");
            return true;
        }
        return false;
    }


    public void addTransaction(double amount, String category, LocalDate date) {
        e = new Expense(user.getId());
        e.setAmount(amount);
        e.setDate(date);
        e.setCategory(category);
        expenseDAO.save(e);

        if (this.cycle != null && this.cycle.isActive()) {
            Remaining -= amount;
            double spent = sum_of_transactions(getExpenseList());
            // Fire events automatically — listeners react without BudgetManager
            // knowing who they are or what they will do
            evaluateAndNotify(spent);
        } else {
            System.err.println("Warning: No active cycle. " +
                    "Expense recorded but not tracked against a budget.");
        }
    }

    public Map<String, Double> getSpendingByCategory() {
        ExpenseDAO E = new ExpenseDAO();
        return E.categorySpendingQuery();
    }

    public void startCycle(double limit, LocalDate d1, LocalDate d2) {
        cycle.setCycle(limit, d1, d2);
        Remaining = limit;
    }

    public void cancelCycle() {
        cycle.resetCycle();
    }

    public boolean checkCycleFinish() {
        return cycle.getEndDate().isBefore(LocalDate.now());
    }

    public LocalDate getCycleStartDate() { return cycle.getStartDate(); }
    public LocalDate getCycleEndDate()   { return cycle.getEndDate();   }

}