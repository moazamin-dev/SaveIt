package com.saveit.service;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Expense;
import com.saveit.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @brief Service class managing business logic for expense operations.
 *
 * This class coordinates the creation, retrieval, and deletion of expenses
 * by interacting with the ExpenseDAO and filtering data based on the user's
 * current financial cycle.
 */
public class ExpenseService {

    /** @var User user The user associated with this service instance */
    private final User user;

    /** @var ExpenseDAO expenseDAO The data access object for expense database operations */
    private final ExpenseDAO expenseDAO;

    /** @var Expense e Temporary expense reference used during transaction processing */
    private Expense e;

    /**
     * @brief Constructs an ExpenseService for a specific user.
     * @param user The User object owning the expenses.
     */
    public ExpenseService(User user) {
        this.user = user;
        this.expenseDAO = new ExpenseDAO();
    }

    /**
     * @brief Retrieves a list of expenses relevant to the current active cycle.
     *
     * If an active cycle exists, it returns expenses starting from the cycle's
     * start date. Otherwise, it returns all expenses for the user.
     *
     * @return List<Expense> A list of filtered or total expenses.
     */
    public List<Expense> getExpenseList() {
        com.saveit.model.Cycle currentCycle = new com.saveit.dao.CycleDAO().getCycle(user.getId());

        if (currentCycle == null || currentCycle.getStartDate() == null) {
            return expenseDAO.getAll(user.getId());
        }

        return expenseDAO.getExpenseSinceDate(user.getId(), currentCycle.getStartDate().toString());
    }

    /**
     * @brief Creates and saves a new transaction (expense).
     *
     * @param amount The monetary value of the transaction.
     * @param category The name of the category to resolve and assign.
     * @param date The date the transaction occurred.
     */
    public void addTransaction(double amount, String category, LocalDate date) {
        e = new Expense(user.getId());
        e.setAmount(amount);
        e.setDate(date);
        e.setCategory(category);
        expenseDAO.save(e);
    }

    /**
     * @brief Retrieves the total spending grouped by category name.
     * @return Map<String, Double> A map of category names to their total spent amounts.
     */
    public Map<String, Double> getSpendingByCategory() {
        return expenseDAO.categorySpendingQuery(user.getId());
    }

    /**
     * @brief Deletes a specific expense record.
     * @param e The Expense object to be removed from the database.
     */
    public void deleteExpense(Expense e) {
        expenseDAO.delete(e.getId());
    }

    /**
     * @brief Retrieves every expense record associated with the user.
     * @return List<Expense> The full history of expenses.
     */
    public List<Expense> getAllExpenses() {
        return expenseDAO.getAll(user.getId());
    }
}