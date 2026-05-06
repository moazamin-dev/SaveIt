package com.saveit.controller;

import com.saveit.service.BudgetManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class BudgetSetupController extends Controller {

    @FXML private VBox budget_setup_root;
    @FXML private TextField limitField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private BudgetManager manager;

    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());

        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusMonths(1));
    }

    @FXML
    private void handleStartCycle() {
        manager.cancelCycle();
        try {
            String limitText = limitField.getText().replace("$", "").replace(",", "");
            double limit = Double.parseDouble(limitText);

            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (endDate.isBefore(startDate)) {
                System.err.println("Error: End date cannot be before start date.");
                return;
            }

            manager.startCycle(limit, startDate, endDate);

            navigateToDashboard();

        } catch (NumberFormatException e) {
            System.err.println("Error: Please enter a valid numeric limit.");
        }
    }

    private void navigateToDashboard() {
        Parent dashboardView = SceneController.getInstance().loadScene(ViewType.DASHBOARD);

        if (dashboardView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) limitField.getScene().getWindow();
            stage.getScene().setRoot(dashboardView);
        }
    }

}