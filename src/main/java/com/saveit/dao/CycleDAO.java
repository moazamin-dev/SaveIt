package com.saveit.dao;

import com.saveit.model.Cycle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CycleDAO {

    private Connection connection;
    public CycleDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}
    public void save(int user_id,Cycle cycle) {
        String Query = "INSERT INTO Cycle(user_id, monthly_limit, start_date, end_date) VALUES(?,?,?,?)";
        try(PreparedStatement save = connection.prepareStatement(Query)){
            save.setInt(1, user_id);
            save.setDouble(2, cycle.getLimit());
            save.setString(3, cycle.getStartDate().toString());
            save.setString(4,cycle.getEndDate().toString());

            save.executeUpdate();
            System.out.println("cycle added successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error while saving cycles: " + ex.getMessage());
        }
    }


    public Cycle getCycle(int User_id) {
        String Query = "SELECT * FROM Cycle WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement getCycle = connection.prepareStatement(Query)) {
            getCycle.setInt(1, User_id);
            try (ResultSet rs = getCycle.executeQuery()) {
                if (rs.next()) {
                    Cycle cycle = new Cycle();
                    cycle.setId(rs.getInt("id"));
                    String startDateStr = rs.getString("start_date");
                    String endDateStr = rs.getString("end_date");
                    cycle.setCycle(rs.getDouble("monthly_limit"),
                            startDateStr != null ? LocalDate.parse(startDateStr) : null,
                            endDateStr != null ? LocalDate.parse(endDateStr) : null);
                    return cycle;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while finding cycle: " + ex.getMessage());
        }
        return null;
    }

    public void delete(int id) {
        String Query = "DELETE FROM Cycle WHERE id = ?";
        try(PreparedStatement delete = connection.prepareStatement(Query)){

            delete.setInt(1,id);

            delete.executeUpdate();
            System.out.println("cycle deleted successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error when deleting: " + ex.getMessage());

        }
    }
}