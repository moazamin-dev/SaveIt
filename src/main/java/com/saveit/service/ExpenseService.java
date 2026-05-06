package com.saveit.service;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Expense;
import com.saveit.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    private final User user;
    private final ExpenseDAO expenseDAO;
    private Expense e;

    public ExpenseService(User user) {
        this.user = user;
        this.expenseDAO = new ExpenseDAO();
    }

    public List<Expense> getExpenseList() {
        return expenseDAO.getExpenseSinceDate(user.getId(),
                new com.saveit.dao.CycleDAO().getCycle(user.getId()).getStartDate().toString());
    }

    public void addTransaction(double amount, String category, LocalDate date) {
        e = new Expense(user.getId());
        e.setAmount(amount);
        e.setDate(date);
        e.setCategory(category);
        expenseDAO.save(e);
    }

    public Map<String, Double> getSpendingByCategory() {
        return expenseDAO.categorySpendingQuery(user.getId());
    }

    public void deleteExpense(Expense e) {
        expenseDAO.delete(e.getId());
    }
}