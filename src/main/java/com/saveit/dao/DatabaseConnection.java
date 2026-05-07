package com.saveit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @brief Singleton class to manage the database connection and schema initialization.
 *
 * This class ensures a single connection to the SQLite database is maintained throughout
 * the application's lifecycle and handles the creation of necessary tables.
 */
public class DatabaseConnection {

    /** @var DatabaseConnection instance The single static instance of this class */
    private static DatabaseConnection instance;

    /** @var Connection connection The active JDBC connection object */
    private Connection connection;

    /** @var String url The JDBC connection URL for the SQLite database file */
    private String url = "jdbc:sqlite:saveit_app.db";

    /**
     * @brief Private constructor to prevent external instantiation.
     *
     * Initializes the database connection and triggers the creation of tables
     * if they do not already exist.
     */
    private DatabaseConnection() {
        try{
            this.connection = DriverManager.getConnection(url);
            initializeDatabase();
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * @brief Provides global access to the DatabaseConnection instance.
     *
     * Implements the Singleton pattern to ensure only one database connection is opened.
     *
     * @return DatabaseConnection The singleton instance.
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * @brief Gets the active SQL connection.
     * @return Connection The current JDBC connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @brief Creates the database schema if it does not exist.
     *
     * Defines and executes SQL statements to create the User, Category, Expense,
     * and Cycle tables. It also enables foreign key constraints for the connection.
     */
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