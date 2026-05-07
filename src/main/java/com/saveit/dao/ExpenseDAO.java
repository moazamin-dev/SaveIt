package com.saveit.dao;

import com.saveit.model.Expense;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @brief Data Access Object for Expense entities.
 *
 * This class provides implementation for managing Expense records in the database,
 * including methods to create, retrieve, filter, and delete expense entries.
 */
public class ExpenseDAO implements DAO<Expense> {

    /** @var Connection connection The database connection instance */
    private Connection connection;

    /**
     * @brief Constructor for ExpenseDAO.
     *
     * Initializes the database connection using the DatabaseConnection singleton.
     */
    public ExpenseDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}

    /**
     * @brief Saves a new Expense record to the database.
     *
     * @param e The Expense object containing the details to be persisted.
     */
    @Override
    public void save(Expense e) {
        String Query = "INSERT INTO Expense(amount,date,user_id,category_id) VALUES(?,?,?,?)";
        try(PreparedStatement save = connection.prepareStatement(Query)){
            save.setDouble(1, e.getAmount());
            save.setString(2,e.getDate().toString());
            save.setInt(3, e.getUser_id());
            save.setInt(4,e.getCategoryID());

            save.executeUpdate();
            System.out.println("expense added successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error while saving expense: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * @brief Retrieves all expenses associated with a specific user.
     *
     * Joins with the Category table to resolve category names for each expense.
     *
     * @param id The ID of the user whose expenses are being retrieved.
     * @return List<Expense> A list of Expense objects.
     */
    @Override
    public List<Expense> getAll(int id) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT e.*, c.category_name AS category_name FROM Expense e " +
                "JOIN Category c ON e.category_id = c.id " +
                "WHERE e.user_id = ?";

        try (PreparedStatement getAll = connection.prepareStatement(sql)) {
            getAll.setInt(1, id);

            try (ResultSet rs = getAll.executeQuery()) {
                while (rs.next()) {
                    Expense e = new Expense(id);
                    e.setId(rs.getInt("id"));
                    e.setAmount(rs.getDouble("amount"));

                    e.setCategory(rs.getString("category_name"));

                    String dateStr = rs.getString("date");
                    e.setDate(dateStr != null ? LocalDate.parse(dateStr) : null);

                    expenses.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while finding expenses: " + ex.getMessage());
            ex.printStackTrace();
        }
        return expenses;
    }

    /**
     * @brief Retrieves expenses for a specific user that occurred on or after a given date.
     *
     * @param user_id The ID of the user.
     * @param date The ISO-8601 date string (YYYY-MM-DD) representing the starting threshold.
     * @return List<Expense> A filtered list of Expense objects.
     */
    public List<Expense> getExpenseSinceDate(int user_id, String date) {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT e.*,c.category_name AS category_name FROM Expense e " +
                "JOIN Category c ON e.category_id = c.id " +
                "WHERE e.user_id = ? AND e.date >= ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, user_id);
            ps.setString(2, date);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense e = new Expense(user_id);
                    e.setId(rs.getInt("id"));
                    e.setAmount(rs.getDouble("amount"));
                    e.setCategory(rs.getString("category_name"));
                    String dateStr = rs.getString("date");
                    e.setDate(dateStr != null ? LocalDate.parse(dateStr) : null);
                    expenses.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error in getExpenseSinceDate: " + ex.getMessage());
            ex.printStackTrace();
        }
        return expenses;
    }

    /**
     * @brief Calculates total spending grouped by category for a specific user.
     *
     * @param user_id The ID of the user.
     * @return Map<String, Double> A map where keys are category names and values are the sum of expenses.
     */
    public Map<String,Double> categorySpendingQuery(int user_id){
        Map<String,Double> result = new HashMap<>();
        String Query = "SELECT c.category_name, SUM(e.amount) as total " +
                "FROM Category c " +
                "JOIN Expense e ON c.id = e.category_id AND e.user_id = ? " +
                "GROUP BY c.category_name";
        try(PreparedStatement pst = connection.prepareStatement(Query);) {
            pst.setInt(1, user_id);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("category_name"), rs.getDouble("total"));
                }
            }
        }
        catch (SQLException ex) {
            System.err.println("Database Error while getting Categories Spending: " + ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @brief Deletes a specific expense record by its unique ID.
     *
     * @param id The unique ID of the expense to delete.
     */
    @Override
    public void delete(int id) {
        String Query = "DELETE FROM Expense WHERE id = ?";
        try(PreparedStatement delete = connection.prepareStatement(Query)){

            delete.setInt(1,id);

            delete.executeUpdate();
            System.out.println("Row deleted successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error when deleting expense: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}