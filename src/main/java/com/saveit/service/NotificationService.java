package com.saveit.service;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class NotificationService implements BudgetListener {

    // ── Severity ──────────────────────────────────────────────────────────────

    public enum Severity { NONE, INFO, WARNING, CRITICAL }

    // ── State ─────────────────────────────────────────────────────────────────

    private String   lastMessage  = "";
    private String   lastTitle    = "";
    private Severity lastSeverity = Severity.NONE;

    // ── BudgetListener implementation ─────────────────────────────────────────

    @Override
    public void onBudgetEvent(BudgetEvent event) {
        switch (event.getType()) {
            case BUDGET_EXCEEDED -> notifyCritical(
                    "Budget exceeded",
                    String.format(
                            "You have spent $%.0f — $%.0f over your $%.0f limit.",
                            event.getAmountSpent(),
                            event.getAmountSpent() - event.getLimit(),
                            event.getLimit()));

            case BUDGET_WARNING -> notifyWarning(
                    "Approaching limit",
                    String.format(
                            "You have spent $%.0f of your $%.0f limit (%.0f%%). Consider slowing down.",
                            event.getAmountSpent(),
                            event.getLimit(),
                            event.getRatio() * 100));

            case BUDGET_OK -> updateStatus(
                    "Budget is healthy",
                    String.format(
                            "You have spent $%.0f of your $%.0f limit (%.0f%%). On track.",
                            event.getAmountSpent(),
                            event.getLimit(),
                            event.getRatio() * 100),
                    Severity.INFO);
        }
    }

    // ── Core API ──────────────────────────────────────────────────────────────

    public void notify(String title, String message, Severity severity) {
        this.lastTitle    = title;
        this.lastMessage  = message;
        this.lastSeverity = severity;

        showPopup(title, message, toAlertType(severity));
    }

    public void updateStatus(String title, String message, Severity severity) {
        this.lastTitle    = title;
        this.lastMessage  = message;
        this.lastSeverity = severity;
    }

    public void showPopup(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ── Convenience overloads ─────────────────────────────────────────────────

    public void notifyInfo(String title, String message) {
        notify(title, message, Severity.INFO);
    }

    public void notifyWarning(String title, String message) {
        notify(title, message, Severity.WARNING);
    }

    public void notifyCritical(String title, String message) {
        notify(title, message, Severity.CRITICAL);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String   getLastMessage()  { return lastMessage;  }
    public String   getLastTitle()    { return lastTitle;    }
    public Severity getLastSeverity() { return lastSeverity; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AlertType toAlertType(Severity severity) {
        return switch (severity) {
            case WARNING  -> AlertType.WARNING;
            case CRITICAL -> AlertType.ERROR;
            default       -> AlertType.INFORMATION;
        };
    }
}