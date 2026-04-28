package com.budget.dao;

import java.sql.Connection;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private String url;

    private DatabaseConnection() {
        // TODO: implement
    }

    public static DatabaseConnection getInstance() {
        // TODO: implement
        return null;
    }

    public Connection getConnection() {
        // TODO: implement
        return null;
    }

    public void initializeDatabase() {
        // TODO: implement
    }
}
