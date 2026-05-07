package com.saveit.controller;

import com.saveit.service.BudgetManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

/**
 * @brief Controller for the Budget Setup view.
 *
 * This class manages the user interface for initializing a new financial budget cycle.
 * It handles input for the budget limit and the start/end dates, ensuring the data
 * is validated before persisting it via the BudgetManager.
 */
public class BudgetSetupController extends Controller {

    /** @var VBox budget_setup_root The root container for the budget setup UI */
    @FXML private VBox budget_setup_root;

    /** @var TextField limitField Input field for the user's total budget limit */
    @FXML private TextField limitField;

    /** @var DatePicker startDatePicker Selector for the cycle's start date */
    @FXML private DatePicker startDatePicker;

    /** @var DatePicker endDatePicker Selector for the cycle's end date */
    @FXML private DatePicker endDatePicker;

    /** @var BudgetManager manager The business logic service for budget operations */
    private BudgetManager manager;

    /**
     * @brief Initializes the controller and sets default date values.
     *
     * This method is called automatically after the FXML file is loaded. It sets the
     * start date to today and the end date to one month from today by default.
     */
    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());

        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusMonths(1));
    }

    /**
     * @brief Handles the action to start a new budget cycle.
     *
     * It parses the limit input, validates that the end date is not before the
     * start date, and replaces any existing cycle with the new parameters.
     * Upon success, it triggers navigation to the dashboard.
     */
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

    /**
     * @brief Navigates the application view to the Dashboard.
     *
     * Loads the dashboard scene via the SceneController and updates the current
     * stage root to display it.
     */
    private void navigateToDashboard() {
        Parent dashboardView = SceneController.getInstance().loadScene(ViewType.DASHBOARD);

        if (dashboardView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) limitField.getScene().getWindow();
            stage.getScene().setRoot(dashboardView);
        }
    }

}