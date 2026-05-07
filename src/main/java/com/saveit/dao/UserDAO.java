package com.saveit.dao;

import com.saveit.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @brief Data Access Object for User entities.
 *
 * This class handles database operations related to user management, including
 * user registration, profile retrieval, and credential verification.
 */
public class UserDAO {

    /** @var Connection connection The active database connection */
    private Connection connection;

    /**
     * @brief Constructor for UserDAO.
     *
     * Initializes the database connection using the DatabaseConnection singleton.
     */
    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * @brief Registers a new user in the database.
     *
     * Hashes the provided plain-text password before storing it for security.
     *
     * @param u The User object containing profile details.
     * @param plainPassword The raw password string to be hashed and stored.
     * @return boolean True if registration was successful, false if the username exists or an error occurred.
     */
    public boolean register(User u, String plainPassword) {
        String sql = "INSERT INTO User (name, phone, username, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, u.getName());
            pstmt.setString(2, u.getPhone());
            pstmt.setString(3, u.getUname());
            pstmt.setString(4, u.hashPassword(plainPassword));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("User already exists: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * @brief Retrieves a User object based on their unique username.
     *
     * @param username The username to search for.
     * @return User The User object if found, or null if no such user exists.
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setUname(rs.getString("username"));

                return user;
            }
        } catch (SQLException e) {
            System.err.println("Database Error while finding user: " + e.getMessage());
        }
        return null;
    }

    /**
     * @brief Retrieves the hashed password stored in the database for a specific user.
     *
     * Used primarily during the login process to compare against provided credentials.
     *
     * @param username The username of the user.
     * @return String The Base64 encoded hashed password, or null if not found.
     */
    public String getStoredPassword(String username) {
        String sql = "SELECT password FROM User WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                return password;
            }
        } catch (SQLException e) {
            System.err.println("Database Error while finding user: " + e.getMessage());
        }
        return null;
    }
}