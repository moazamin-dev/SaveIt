package com.saveit.dao;

import com.saveit.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection connection;
    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean register(User u, String plainPassword) {
        String sql = "INSERT INTO users (name, phone, username, password) VALUES (?, ?, ?, ?)";
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

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
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

    public String getStoredPassword(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";
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

    public void updateProfile(User u) {

    }
}
