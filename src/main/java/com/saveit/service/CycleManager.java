package com.saveit.service;

import com.saveit.model.Cycle;
import java.time.LocalDate;

public class CycleManager {

    private Cycle cycle;

    public CycleManager(Cycle cycle) {
        this.cycle = cycle;
    }

    public Cycle getCycle() { return cycle; }

    public double getCycleLimit() {
        return cycle.getLimit();
    }

    public void startCycle(double limit, LocalDate d1, LocalDate d2) {
        cycle.setCycle(limit, d1, d2);
    }

    public void cancelCycle() {
        cycle.resetCycle();
    }

    public boolean checkCycleFinish() {
        LocalDate currentDate = LocalDate.now();
        if (cycle.getEndDate().isBefore(currentDate)) {
            return true;
        }
        return false;
    }

    public LocalDate getCycleStartDate() { return cycle.getStartDate(); }
    public LocalDate getCycleEndDate()   { return cycle.getEndDate(); }
}