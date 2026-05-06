package com.saveit.service;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Category;
import com.saveit.model.Expense;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    private ExpenseDAO expenseDAO = new ExpenseDAO();

    public Map<Category, Double> getCategoryDistribution(int userId) {
        List<Expense> expenses = expenseDAO.getAll(userId); // Fetches from DB
        
        // Groups expenses by category and sums the amounts[cite: 19]
        return expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)
            ));
    }

    public Map<LocalDate, Double> getWeeklySpendingTrend(int userId) {
        // Calculate date for 7 days ago
        String sevenDaysAgo = LocalDate.now().minusDays(7).toString();
        
        // Uses the specialized DAO method you already wrote[cite: 16]
        List<Expense> expenses = expenseDAO.getExpenseSinceDate(userId, sevenDaysAgo);

        return expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getDate,
                Collectors.summingDouble(Expense::getAmount)
            ));
    }
    public Map<LocalDate, Double> getMonthlySpendingTrend(int userId) {
    // Fetches all expenses for the user from the DAO
    List<Expense> expenses = expenseDAO.getAll(userId); 

    // Groups by the first day of the month to create a monthly trend map
    return expenses.stream()
        .collect(Collectors.groupingBy(
            e -> e.getDate().withDayOfMonth(1), 
            Collectors.summingDouble(Expense::getAmount)
        ));
}
}