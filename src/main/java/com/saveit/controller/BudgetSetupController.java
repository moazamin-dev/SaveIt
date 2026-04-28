package com.budget.controller;

import com.budget.service.BudgetManager;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class BudgetSetupController extends Controller {

    private TextField limitField;
    private DatePicker startPicker;
    private DatePicker endPicker;
    private BudgetManager manager;

    public void handleStartCycle() {
        // TODO: implement
    }

    private boolean validateDates() {
        // TODO: implement
        return false;
    }

    @Override
    public void initialize() {
        // TODO: implement
    }

    @Override
    public Node getViewNodes() {
        // TODO: implement
        return null;
    }
}
