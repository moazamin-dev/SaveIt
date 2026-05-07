package com.saveit.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @brief Controller for the application's sidebar navigation menu.
 *
 * This class manages the sidebar UI, including displaying the current user's name,
 * handling navigation between different views based on button clicks, and managing
 * the visual "active" state of navigation buttons.
 */
public class SideBarController extends Controller {

    /** @var Label usernameLabel Label that displays the name of the logged-in user */
    @FXML private Label usernameLabel;

    /**
     * @brief Initializes the sidebar with the current user's information.
     *
     * Fetches the name from the User object associated with this controller
     * and updates the UI label.
     */
    @Override
    public void initialize() {
        String name = getUser().getName();
        usernameLabel.setText(name);
    }

    /**
     * @brief Navigates the application to a specific view.
     *
     * Uses the SceneController to load the requested FXML and updates the
     * primary stage's root node.
     *
     * @param viewName The ViewType enum representing the target screen.
     */
    public void navBtnClicked(ViewType viewName) {
        try {
            ViewType targetView = viewName;

            Parent nextView = SceneController.getInstance().loadScene(targetView);

            if (nextView != null) {
                javafx.stage.Stage stage = (javafx.stage.Stage) usernameLabel.getScene().getWindow();
                stage.getScene().setRoot(nextView);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid View Name: " + viewName);
        }
    }

    /**
     * @brief Logs the user out and returns to the login screen.
     *
     * Resets the application root to the Login view.
     */
    @FXML
    private void handleLogout() {
        Parent login = SceneController.getInstance().loadScene(ViewType.LOGIN);
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.getScene().setRoot(login);
    }

    /**
     * @brief Handles navigation button events from the sidebar.
     *
     * This method updates the CSS style classes to reflect the "active" button
     * and maps the Button ID (e.g., "dashboardBtn") to a ViewType enum
     * (e.g., DASHBOARD) to trigger navigation.
     *
     * @param event The ActionEvent triggered by clicking a navigation button.
     */
    @FXML
    private void handleNavigation(javafx.event.ActionEvent event) {
        javafx.scene.control.Button clickedBtn = (javafx.scene.control.Button) event.getSource();
        javafx.scene.layout.VBox parent = (javafx.scene.layout.VBox) clickedBtn.getParent();

        for (Node node : parent.getChildren()) {
            if (node instanceof javafx.scene.control.Button) {
                node.getStyleClass().remove("active");
            }
        }
        clickedBtn.getStyleClass().add("active");

        try {
            String enumName = clickedBtn.getId().replace("Btn", "").toUpperCase();

            ViewType view = ViewType.valueOf(enumName);

            navBtnClicked(view);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: No Enum constant found for Button ID: " + clickedBtn.getId());
        }
    }
}