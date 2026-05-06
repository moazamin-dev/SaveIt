package com.saveit.model;

import java.time.LocalDate;
import java.util.Objects;

public class Cycle {
    private double limit = 0;
    private LocalDate startDate = LocalDate.of(2000,1,1);
    private LocalDate endDate = LocalDate.of(2100,1,1);

    public Cycle(){
        limit = 0;
        startDate = LocalDate.of(2000,1,1);
        endDate = LocalDate.of(2100,1,1);
    }

    public void setCycle(double limit, LocalDate d1, LocalDate d2){
        this.limit = limit;
        this.startDate = d1;
        this.endDate = d2;
    }

    public void resetCycle(){
        limit = 0;
        startDate = LocalDate.of(2000,1,1);
        endDate = LocalDate.of(2100,1,1);
    }

    public double getLimit() {
        return limit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return this.limit > 0 && !Objects.equals(this.startDate, LocalDate.of(2000, 1, 1));
    }
}
