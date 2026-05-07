package com.saveit.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @brief Represents a financial or temporal cycle with a specific budget limit and date range.
 *
 * This class manages the lifecycle of a cycle, including its active status,
 * period boundaries, and associated spending limits.
 */
public class Cycle {

    /** @var int id Unique identifier for the cycle */
    private int id;

    /** @var double limit The monetary or unit limit for this cycle */
    private double limit = 0;

    /** @var LocalDate startDate The date when the cycle begins */
    private LocalDate startDate = LocalDate.of(2000,1,1);

    /** @var LocalDate endDate The date when the cycle expires */
    private LocalDate endDate = LocalDate.of(2100,1,1);

    /**
     * @brief Default constructor initializing the cycle with default values.
     */
    public Cycle(){
        limit = 0;
        startDate = LocalDate.of(2000,1,1);
        endDate = LocalDate.of(2100,1,1);
    }

    /**
     * @brief Sets the cycle parameters in a single call.
     *
     * @param limit The maximum limit for the cycle.
     * @param d1 The start date of the cycle.
     * @param d2 The end date of the cycle.
     */
    public void setCycle(double limit, LocalDate d1, LocalDate d2){
        this.limit = limit;
        this.startDate = d1;
        this.endDate = d2;
    }

    /**
     * @brief Sets the unique identifier for the cycle.
     * @param id The ID to assign.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @brief Gets the unique identifier of the cycle.
     * @return int The cycle ID.
     */
    public int getId(){
        return id;
    }

    /**
     * @brief Resets the cycle to its default uninitialized state.
     */
    public void resetCycle(){
        limit = 0;
        startDate = LocalDate.of(2000,1,1);
        endDate = LocalDate.of(2100,1,1);
    }

    /**
     * @brief Gets the current limit of the cycle.
     * @return double The limit value.
     */
    public double getLimit() {
        return limit;
    }

    /**
     * @brief Gets the start date of the cycle.
     * @return LocalDate The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @brief Gets the end date of the cycle.
     * @return LocalDate The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @brief Checks if the cycle is currently active.
     *
     * A cycle is considered active if the limit is greater than 0 and the
     * start date has been modified from its default value.
     *
     * @return boolean True if active, false otherwise.
     */
    public boolean isActive() {
        return this.limit > 0 && !Objects.equals(this.startDate, LocalDate.of(2000, 1, 1));
    }
}