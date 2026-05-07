package com.saveit.service;

import com.saveit.dao.CycleDAO;
import com.saveit.model.Cycle;
import com.saveit.model.User;

import java.time.LocalDate;

/**
 * @brief Service class responsible for managing the lifecycle of financial cycles.
 *
 * This class handles the logic for starting, canceling, and checking the status
 * of budget cycles. It acts as an intermediary between the Cycle model and the
 * CycleDAO database operations.
 */
public class CycleManager {

    /** @var CycleDAO cycleDAO Data access object for cycle-related database operations */
    CycleDAO cycleDAO = new CycleDAO();

    /** @var Cycle cycle The current active or most recent cycle object */
    private Cycle cycle;

    /** @var User user The user associated with this manager */
    private final User user;

    /**
     * @brief Constructs a CycleManager and loads the latest cycle for the user.
     *
     * If no cycle is found in the database, a new default Cycle instance is created.
     *
     * @param user The User object owning the cycles.
     */
    public CycleManager(User user) {
        this.user = user;
        this.cycle = cycleDAO.getCycle(user.getId());
        if (this.cycle == null) {
            this.cycle = new Cycle();
        }
    }

    /**
     * @brief Retrieves the current cycle object.
     * @return Cycle The current cycle.
     */
    public Cycle getCycle() { return cycle; }

    /**
     * @brief Retrieves the budget limit of the current cycle.
     * @return double The cycle limit, or 0.0 if no cycle exists.
     */
    public double getCycleLimit() {
        if (cycle == null) return 0.0;
        return cycle.getLimit();
    }

    /**
     * @brief Initiates a new cycle for the user.
     *
     * This method cancels any existing cycle before setting the new limit
     * and date range, then persists the new cycle to the database.
     *
     * @param limit The maximum spending amount for the cycle.
     * @param d1 The start date of the cycle.
     * @param d2 The end date of the cycle.
     */
    public void startCycle(double limit, LocalDate d1, LocalDate d2) {
        cancelCycle();
        cycle.setCycle(limit, d1, d2);
        cycleDAO.save(user.getId(),cycle);
    }

    /**
     * @brief Cancels the current cycle and removes it from the database.
     *
     * The internal cycle object is reset to its default uninitialized state.
     */
    public void cancelCycle() {
        if (cycle != null && cycle.getId() != 0) { // Check if it actually exists in DB
            cycleDAO.delete(cycle.getId());
        }
        if (cycle == null) {
            cycle = new Cycle();
        }
        cycle.resetCycle();
    }

    /**
     * @brief Checks if the current cycle has reached its conclusion.
     *
     * @return boolean True if today's date is after the cycle's end date, false otherwise.
     */
    public boolean checkCycleFinish() {
        LocalDate currentDate = LocalDate.now();
        if (cycle.getEndDate().isBefore(currentDate)) {
            return true;
        }
        return false;
    }

    /**
     * @brief Gets the start date of the current cycle.
     * @return LocalDate The start date, or null if no cycle is loaded.
     */
    public LocalDate getCycleStartDate() {
        if (cycle == null) return null;
        return cycle.getStartDate();
    }

    /**
     * @brief Gets the end date of the current cycle.
     * @return LocalDate The end date, or null if no cycle is loaded.
     */
    public LocalDate getCycleEndDate()   {
        if (this.cycle == null) {
            return null;
        }
        return cycle.getEndDate(); }
}