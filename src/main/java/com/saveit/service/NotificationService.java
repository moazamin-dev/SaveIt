package com.saveit.service;

import javafx.application.Platform;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

/**
 * @brief Service responsible for delivering visual alerts and budget status updates.
 *
 * This class monitors the percentage of budget consumption and triggers
 * JavaFX-based desktop notifications to inform the user when specific
 * thresholds (e.g., 70% or 100%) are reached.
 */
public class NotificationService {

    /**
     * @brief Evaluates current spending against the limit and triggers alerts if necessary.
     *
     * If spending reaches 70% of the limit, a warning is shown. If spending
     * meets or exceeds 100%, a critical stop alert is displayed.
     *
     * @param currentTotal The total amount spent in the current cycle.
     * @param limit The maximum budget limit set for the cycle.
     */
    public void checkBudgetStatus(double currentTotal, double limit) {
        if (limit <= 0) return;

        double usage = currentTotal / limit;

        if (usage >= 1.0) {
            showPopup("STOP! You have exceeded your limit of $" + limit);
        } else if (usage >= 0.70) {
            showPopup("Warning: You have used " + (int)(usage * 100) + "% of your budget.");
        }
    }

    /**
     * @brief Displays a graphical popup notification on the screen.
     *
     * This method uses Platform.runLater to ensure the notification is
     * rendered on the JavaFX Application Thread. The popup appears in the
     * bottom-right corner of the screen.
     *
     * @param message The text content to display in the notification.
     */
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