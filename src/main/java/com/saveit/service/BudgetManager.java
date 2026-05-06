package com.saveit.service;

import com.saveit.dao.CycleDAO;
import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Cycle;
import com.saveit.model.Expense;
import com.saveit.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BudgetManager {

    private User user;
    private ExpenseDAO expenseDAO;
    private Cycle cycle;
    private Expense e;
    private double dailyLimit;
    private double Remaining;

    public BudgetManager(User user) {
        this.user = user;
        CycleDAO cycleDAO = new CycleDAO();
        this.cycle = cycleDAO.getCycle(user.getId()); //PlaceHolder
        expenseDAO = new ExpenseDAO();

        if (this.cycle != null && this.cycle.isActive()) {
            double spent = sum_of_transactions(getExpenseList());
            this.Remaining = this.cycle.getLimit() - spent;
        } else {
            this.Remaining = 0;
        }
    }

    public double getRemainingLimit() {
        return Remaining;
    }

    public double getCycleLimit() {
        return cycle.getLimit();
    }

    public List<Expense> getExpenseList() {
        return expenseDAO.getExpenseSinceDate(user.getId(), cycle.getStartDate().toString());
    }

    public double calculateDailyLimit() {
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), cycle.getEndDate());
        if (daysLeft <= 0) return getRemainingLimit();
        dailyLimit = getRemainingLimit() / daysLeft;
        return dailyLimit;
    }

    public double sum_of_transactions(List<Expense> list) {
        double sum = 0;
        for (Expense e : list) {
            sum += e.getAmount();
        }
        return sum;
    }

    public boolean isOverBudget() {
        if(cycle == null){return false;}
        if ( Remaining <= 0) {
            NotificationService NS = new NotificationService();
            NS.checkBudgetStatus(sum_of_transactions(getExpenseList()), cycle.getLimit());
            NS.showPopup("Monthly limit Exceeded!");
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



        if (this.cycle != null && this.cycle.isActive()){
            Remaining -= amount;
            isOverBudget();

        }
        else{
            System.err.println("Warning: No active cycle. Expense recorded but not tracked against a budget.");
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
        LocalDate currentDate = LocalDate.now();
        if (cycle.getEndDate().isBefore(currentDate)) {
            return true;
        }
        return false;
    }

    public LocalDate getCycleStartDate() {
        return cycle.getStartDate();
    }

    public LocalDate getCycleEndDate() {
        return cycle.getEndDate();
    }
}
