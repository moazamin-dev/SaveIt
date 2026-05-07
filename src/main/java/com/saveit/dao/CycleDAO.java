package com.saveit.dao;

import com.saveit.model.Cycle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @brief Data Access Object for Cycle entities.
 *
 * This class handles all database interactions related to financial cycles,
 * allowing the application to persist, retrieve, and delete budget periods
 * for specific users.
 */
public class CycleDAO {

    /** @var Connection connection The database connection instance */
    private Connection connection;

    /**
     * @brief Constructor for CycleDAO.
     *
     * Initializes the database connection using the DatabaseConnection singleton.
     */
    public CycleDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}

    /**
     * @brief Saves a new Cycle record for a specific user.
     *
     * @param user_id The ID of the user owning this cycle.
     * @param cycle The Cycle object containing the limit and date range.
     */
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

    /**
     * @brief Retrieves the most recent cycle for a specific user.
     *
     * Fetches the latest cycle entry from the database based on the user ID,
     * ordering by ID descending to ensure the current/newest cycle is returned.
     *
     * @param User_id The ID of the user.
     * @return Cycle The latest Cycle object found, or null if no cycle exists for the user.
     */
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

    /**
     * @brief Deletes a specific cycle record by its unique ID.
     *
     * @param id The unique ID of the cycle to remove.
     */
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