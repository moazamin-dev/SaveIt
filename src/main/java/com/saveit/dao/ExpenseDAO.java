package com.saveit.dao;

import com.saveit.model.Expense;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO implements DAO<Expense> {

    private Connection connection;
    public ExpenseDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}

    @Override
    public void save(Expense e) {
        String Query = "INSERT INTO expenses(amount,date,user_id,category_id) VALUES(?,?,?,?)";
        try(PreparedStatement save = connection.prepareStatement(Query)){
            save.setDouble(1, e.getAmount());
            save.setString(2,e.getDate().toString());
            save.setInt(3, e.getUser_id());
            save.setInt(4,e.getCategoryID());

            save.executeUpdate();
            System.out.println("Row added successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error while saving expense: " + ex.getMessage());
        }
    }

    @Override
    public List<Expense> getAll(int id) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT e.*, c.name AS category_name FROM expenses e " +
                "JOIN category c ON e.category_id = c.id " +
                "WHERE e.user_id = ?";

        try (PreparedStatement getAll = connection.prepareStatement(sql)) {
            getAll.setInt(1, id);

            try (ResultSet rs = getAll.executeQuery()) {
                while (rs.next()) {
                    Expense e = new Expense();
                    e.setAmount(rs.getDouble("amount"));

                    e.setCategory(rs.getString("category_name"));

                    String dateStr = rs.getString("date");
                    e.setDate(dateStr != null ? LocalDate.parse(dateStr) : null);

                    expenses.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while finding expenses: " + ex.getMessage());
        }
        return expenses;
    }

    public List<Expense> getExpenseSinceDate(int userId, String date) { // Added userId for security
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT e.*, c.name AS category_name FROM expenses e " +
                "JOIN category c ON e.category_id = c.id " +
                "WHERE e.user_id = ? AND e.date >= ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, date);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Expense e = new Expense();
                    e.setAmount(rs.getDouble("amount"));
                    e.setCategory(rs.getString("category_name")); // Use the joined column
                    String dateStr = rs.getString("date");
                    e.setDate(dateStr != null ? LocalDate.parse(dateStr) : null);
                    expenses.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error in getExpenseSinceDate: " + ex.getMessage());
        }
        return expenses;
    }

    @Override
    public void delete(int id) {
        String Query = "DELETE FROM expenses WHERE id = ?";
        try(PreparedStatement delete = connection.prepareStatement(Query)){

            delete.setInt(1,id);

            delete.executeUpdate();
            System.out.println("Row deleted successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error when deleting: " + ex.getMessage());
        }
    }
}