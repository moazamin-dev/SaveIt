package com.saveit.dao;

import com.saveit.model.Cycle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CycleDAO {

    private Connection connection;

    public CycleDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Cycle cycle) {
        if (cycle.getId() > 0) {
            update(cycle);
        } else {
            insert(cycle);
        }
    }


    private void insert(Cycle cycle) {
        String Query = "INSERT INTO cycles(user_id, monthly_limit, start_date, end_date) VALUES(?,?,?,?)";
        try (PreparedStatement save = connection.prepareStatement(Query)) {
            save.setInt(1, cycle.getUserId());
            save.setDouble(2, cycle.getLimit());
            save.setString(3, cycle.getStartDate().toString());
            save.setString(4, cycle.getEndDate().toString());
            save.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Database Error while inserting cycle: " + ex.getMessage());
        }
    }

    private void update(Cycle cycle) {
        String Query = "UPDATE cycles SET monthly_limit = ?, start_date = ?, end_date = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement update = connection.prepareStatement(Query)) {
            update.setDouble(1, cycle.getLimit());
            update.setString(2, cycle.getStartDate().toString());
            update.setString(3, cycle.getEndDate().toString());
            update.setInt(4, cycle.getId());
            update.setInt(5, cycle.getUserId());
            update.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Database Error while updating cycle: " + ex.getMessage());
        }
    }


    public Cycle getCycle(int userId) {
        String Query = "SELECT * FROM cycles WHERE user_id = ?";
        try (PreparedStatement getCycle = connection.prepareStatement(Query)) {
            getCycle.setInt(1, userId);
            try (ResultSet rs = getCycle.executeQuery()) {
                if (rs.next()) {
                    Cycle cycle = new Cycle(userId);
                    cycle.setId(rs.getInt("id"));
                    cycle.setLimit(rs.getDouble("monthly_limit"));

                    String startDateStr = rs.getString("start_date");
                    if (startDateStr != null) {
                        cycle.setStartDate(LocalDate.parse(startDateStr));
                    }

                    String endDateStr = rs.getString("end_date");
                    if (endDateStr != null) {
                        cycle.setEndDate(LocalDate.parse(endDateStr));
                    }

                    return cycle;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while finding cycle: " + ex.getMessage());
        }
        return new Cycle(userId); // Return empty cycle if none exists
    }


    public void delete(int cycleId) {
        String Query = "DELETE FROM cycles WHERE id = ?";
        try (PreparedStatement delete = connection.prepareStatement(Query)) {
            delete.setInt(1, cycleId);
            delete.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Database Error when deleting cycle: " + ex.getMessage());
        }
    }

    public void deleteByUser(int userId) {
        String Query = "DELETE FROM cycles WHERE user_id = ?";
        try (PreparedStatement delete = connection.prepareStatement(Query)) {
            delete.setInt(1, userId);
            delete.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Database Error when deleting user cycles: " + ex.getMessage());
        }
    }
}