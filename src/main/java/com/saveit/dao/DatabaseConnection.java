package com.saveit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private String url = "jdbc:sqlite:saveit_app.db";

    private DatabaseConnection() {
        try{
            this.connection = DriverManager.getConnection(url);
            initializeDatabase();
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void initializeDatabase() {
        String userTable = "CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, phone TEXT, username TEXT UNIQUE, " +
                "password TEXT, pin INTEGER);";
        String categoryTable = "CREATE TABLE IF NOT EXISTS Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category_name TEXT, user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES User(id) ON DELETE CASCADE);";

        String expenseTable = "CREATE TABLE IF NOT EXISTS Expense (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, date TEXT, user_id INTEGER, category_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES User(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(category_id) REFERENCES Category(id) ON DELETE CASCADE);";

        String cycleTable = "CREATE TABLE IF NOT EXISTS Cycle (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, monthly_limit REAL, start_date TEXT, end_date TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES User(id) ON DELETE CASCADE);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");

            stmt.execute(userTable);
            stmt.execute(categoryTable);
            stmt.execute(expenseTable);
            stmt.execute(cycleTable);

            System.out.println("All database tables (Users, Categories, Expenses, Cycles) initialized.");
        } catch (SQLException e) {
            System.err.println("Table Creation Error: " + e.getMessage());
        }
    }
}
