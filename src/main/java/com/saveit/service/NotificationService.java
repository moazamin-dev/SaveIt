package com.saveit.service;

import javafx.application.Platform;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

public class NotificationService {

    private double lastAlertAmount;

    public void checkBudgetStatus(double currentTotal, double limit) {
        // Safety check: Don't divide by zero!
        if (limit <= 0) return;

        double usage = currentTotal / limit;

        if (usage >= 1.0) {
            showPopup("STOP! You have exceeded your limit of $" + limit);
        } else if (usage >= 0.70) {
            showPopup("Warning: You have used " + (int)(usage * 100) + "% of your budget.");
        }
    }

    public void showPopup(String message) {
        Platform.runLater(() -> {
            Notifications.create()
                    .title("Budget Update")
                    .text(message)
                    .position(Pos.BOTTOM_RIGHT)
                    .showWarning();
        });
    }
}