package com.saveit.model;

import java.time.LocalDate;

public class Cycle {
    private int id;
    private int userId;
    private double limit;
    private LocalDate startDate;
    private LocalDate endDate;

    // Sentinel value to represent "no cycle"
    private static final LocalDate NO_CYCLE_DATE = LocalDate.of(1970, 1, 1);

    public Cycle() {
        this.limit = 0;
        this.startDate = NO_CYCLE_DATE;
        this.endDate = NO_CYCLE_DATE;
        this.userId = -1;
        this.id = -1;
    }

    public Cycle(int userId) {
        this();
        this.userId = userId;
    }

    // Check if this is a REAL cycle (saved in database)
    public boolean existsInDatabase() {
        return id > 0;
    }

    public boolean isActive() {
        return limit > 0 &&
                !startDate.equals(NO_CYCLE_DATE) &&
                !endDate.equals(NO_CYCLE_DATE);
    }

    // Getters and setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public void setCycle(double limit, LocalDate start, LocalDate end) {
        this.limit = limit;
        this.startDate = start;
        this.endDate = end;
    }

    public void resetCycle() {
        this.limit = 0;
        this.startDate = NO_CYCLE_DATE;
        this.endDate = NO_CYCLE_DATE;
    }
}