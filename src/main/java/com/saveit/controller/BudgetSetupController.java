package com.saveit.controller;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Expense;
import com.saveit.model.User;
import com.saveit.service.AuthenticationService;
import com.saveit.service.BudgetEvent;
import com.saveit.service.BudgetListener;
import com.saveit.service.BudgetManager;
import com.saveit.service.NotificationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.util.List;

public class BudgetSetupController extends Controller implements BudgetListener {

    // ── FXML bindings ─────────────────────────────────────────────────────────
    @FXML private TextField   limitField;
    @FXML private DatePicker  startPicker;
    @FXML private DatePicker  endPicker;
    @FXML private Label       errorLabel;
    @FXML private ProgressBar budgetProgress;
    @FXML private Label       spentStatLabel;
    @FXML private Label       remainingStatLabel;
    @FXML private Label       usedPctLabel;
    @FXML private Label       amountLabel;
    @FXML private Label       statusPercentLabel;
    @FXML private Label       remainingLabel;
    @FXML private Circle      notifDot;
    @FXML private Label       notifTitle;
    @FXML private Label       notificationLabel;

    // ── Services / DAOs ───────────────────────────────────────────────────────
    private NotificationService notificationService;
    private BudgetManager       budgetManager;
    private ExpenseDAO          expenseDAO;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public void initialize() {
        notificationService = new NotificationService();
        expenseDAO          = new ExpenseDAO();

        User currentUser = AuthenticationService.getCurrentUser();
        if (currentUser != null) {
            budgetManager = new BudgetManager(currentUser);

            // ── Wire the observer pattern ──────────────────────────────────
            // NotificationService handles popups and state.
            // This controller handles UI updates.
            // BudgetManager knows neither — only the interface.
            budgetManager.addBudgetListener(notificationService);
            budgetManager.addBudgetListener(this);
        }
    }

    @Override
    public void onBudgetEvent(BudgetEvent event) {
        Platform.runLater(() -> refreshNotificationPill());
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    @FXML
    public void handleStartCycle() {
        clearError();
        if (!validateInputs()) return;

        double    limit = parseLimit();
        LocalDate start = startPicker.getValue();
        LocalDate end   = endPicker.getValue();

        // Persist cycle via BudgetManager
        budgetManager.startCycle(limit, start, end);

        // Load current expenses and refresh the full status panel
        refreshStatusPanel(limit, start);
    }

    @FXML
    public void handleReset() {
        limitField.clear();
        startPicker.setValue(null);
        endPicker.setValue(null);
        clearError();
        resetStatusPanel();
    }

    // ── Private: validation ───────────────────────────────────────────────────

    private boolean validateInputs() {
        if (limitField.getText().trim().isEmpty()) {
            displayError("Please enter a monthly limit.");
            return false;
        }
        if (!isValidAmount(limitField.getText().trim())) {
            displayError("Limit must be a positive number.");
            return false;
        }
        return validateDates();
    }

    private boolean validateDates() {
        if (startPicker.getValue() == null) {
            displayError("Please select a cycle start date.");
            return false;
        }
        if (endPicker.getValue() == null) {
            displayError("Please select a cycle end date.");
            return false;
        }
        if (!endPicker.getValue().isAfter(startPicker.getValue())) {
            displayError("End date must be after the start date.");
            return false;
        }
        return true;
    }

    // ── Private: status panel ─────────────────────────────────────────────────


    private void refreshStatusPanel(double limit, LocalDate start) {
        List<Expense> expenses = expenseDAO.getExpenseSinceDate(
                AuthenticationService.getCurrentUser().getId(), start.toString());

        double total     = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double remaining = Math.max(limit - total, 0);
        double ratio     = limit > 0 ? total / limit : 0;

        // Stat cards
        spentStatLabel.setText(String.format("$%.0f", total));
        remainingStatLabel.setText(String.format("$%.0f", remaining));
        usedPctLabel.setText(String.format("%.0f%%", ratio * 100));
        spentStatLabel.getStyleClass().setAll(ratio >= 1.0 ? "stat-val-amber" : "stat-val-green");

        // Progress bar
        budgetProgress.setProgress(Math.min(ratio, 1.0));
        applyProgressStyle(ratio);
        amountLabel.setText(String.format("$%.0f of $%.0f", total, limit));
        remainingLabel.setText(String.format("$%.0f remaining", remaining));

        // Trigger the observer chain:
        // BudgetManager.isOverBudget() → evaluateAndNotify() → fireEvent()
        // → NotificationService.onBudgetEvent() (updates state + popup if needed)
        // → this.onBudgetEvent() (refreshes the notification pill below)
        budgetManager.isOverBudget();
    }

    private void refreshNotificationPill() {
        applyNotificationStyle(notificationService.getLastSeverity());
        notifTitle.setText(notificationService.getLastTitle());
        notificationLabel.setText(notificationService.getLastMessage());

        // Update the progress foot label to reflect current status text
        String footText = switch (notificationService.getLastSeverity()) {
            case WARNING, CRITICAL -> notificationService.getLastTitle();
            default                -> "On track";
        };
        statusPercentLabel.setText(footText);
    }

    private void applyProgressStyle(double ratio) {
        budgetProgress.getStyleClass().removeAll(
                "budget-progress-warn", "budget-progress-danger");
        if (ratio >= 1.0) {
            budgetProgress.getStyleClass().add("budget-progress-danger");
        } else if (ratio >= 0.8) {
            budgetProgress.getStyleClass().add("budget-progress-warn");
        }
    }

    private void applyNotificationStyle(NotificationService.Severity severity) {
        notifDot.getStyleClass().setAll(switch (severity) {
            case INFO     -> "dot-success";
            case WARNING  -> "dot-warn";
            case CRITICAL -> "dot-danger";
            default       -> "dot-neutral";
        });
    }

    private void resetStatusPanel() {
        budgetProgress.setProgress(0);
        budgetProgress.getStyleClass().removeAll(
                "budget-progress-warn", "budget-progress-danger");

        spentStatLabel.setText("—");
        spentStatLabel.getStyleClass().setAll("stat-val-green");
        remainingStatLabel.setText("—");
        usedPctLabel.setText("—");

        amountLabel.setText("— of —");
        statusPercentLabel.setText("");
        remainingLabel.setText("");

        notifDot.getStyleClass().setAll("dot-neutral");
        notifTitle.setText("No data yet");
        notificationLabel.setText("Start a cycle to see spending status.");
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private double parseLimit() {
        return Double.parseDouble(limitField.getText().trim());
    }

    private boolean isValidAmount(String text) {
        try { return Double.parseDouble(text) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    private void displayError(String message) { errorLabel.setText(message); }
    private void clearError()                 { errorLabel.setText(""); }

    @Override
    public Node getViewNodes() { return null; }
}