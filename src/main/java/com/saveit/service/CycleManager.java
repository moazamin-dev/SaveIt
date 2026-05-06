package com.saveit.service;

import com.saveit.dao.CycleDAO;
import com.saveit.model.Cycle;
import com.saveit.model.User;

import java.time.LocalDate;

public class CycleManager {

    CycleDAO cycleDAO = new CycleDAO();
    private Cycle cycle;
    private final User user;

    public CycleManager(User user) {
        this.user = user;
        this.cycle = cycleDAO.getCycle(user.getId());
        if (this.cycle == null) {
            this.cycle = new Cycle();
        }
    }

    public Cycle getCycle() { return cycle; }

    public double getCycleLimit() {
        if (cycle == null) return 0.0;
        return cycle.getLimit();
    }

    public void startCycle(double limit, LocalDate d1, LocalDate d2) {
        cancelCycle();
        cycle.setCycle(limit, d1, d2);
        cycleDAO.save(user.getId(),cycle);
    }

    public void cancelCycle() {
        if (cycle != null && cycle.getId() != 0) { // Check if it actually exists in DB
            cycleDAO.delete(cycle.getId());
        }
        if (cycle == null) {
            cycle = new Cycle();
        }
        cycle.resetCycle();
    }

    public boolean checkCycleFinish() {
        LocalDate currentDate = LocalDate.now();
        if (cycle.getEndDate().isBefore(currentDate)) {
            return true;
        }
        return false;
    }

    public LocalDate getCycleStartDate() {
        if (cycle == null) return null;
        return cycle.getStartDate();
    }

    public LocalDate getCycleEndDate()   {
        if (this.cycle == null) {
            return null;
        }
        return cycle.getEndDate(); }
}