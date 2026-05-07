package com.saveit.controller;

import com.saveit.model.User;
import com.saveit.service.AuthenticationService;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

/**
 * @brief Controller responsible for handling user login and navigation to registration.
 *
 * This class manages the authentication UI, validates user input, and coordinates
 * with the AuthenticationService to verify credentials. Upon successful login,
 * it transitions the application to the main dashboard.
 */
public class LoginController extends Controller {

    /** @var TextField usernameField Input field for the user's username */
    @FXML private TextField usernameField;

    /** @var PasswordField passwordField Input field for the user's password */
    @FXML private PasswordField passwordField;

    /** @var Label errorLabel Display area for authentication feedback or validation errors */
    @FXML private Label errorLabel;

    /** @var AuthenticationService authService Service handling the login business logic */
    private AuthenticationService authService;

    /**
     * @brief Processes the login attempt when the user triggers the login action.
     *
     * Retrieves credentials from the UI, validates that fields are not empty,
     * and attempts authentication. If successful, redirects to the dashboard;
     * otherwise, displays an error message.
     */
    @FXML
    private void handleLogin() {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            displayError("Please fill in all fields.");
            return;
        }

        boolean success = authService.login(username, password);

        if (success) {
            User loggedInUser = AuthenticationService.getCurrentUser();
            openMainDashboard(loggedInUser);
        } else {
            displayError("Invalid username or password.");
        }
    }

    /**
     * @brief Transitions the application view to the dashboard for the authenticated user.
     *
     * Notifies the SceneController of the user update and replaces the current
     * scene root with the Dashboard view.
     *
     * @param user The authenticated User instance.
     */
    private void openMainDashboard(User user) {
        SceneController.getInstance().handleEvent("USER_UPDATED", user);

        Parent dashboard = SceneController.getInstance().loadScene(ViewType.DASHBOARD);

        if (dashboard != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(dashboard);
        }
    }

    /**
     * @brief Initializes the controller by instantiating the AuthenticationService.
     *
     * This method is automatically called by the FXML loader.
     */
    @Override
    public void initialize() {
        authService = new AuthenticationService();
    }

    /**
     * @brief Navigates the UI to the user registration (signup) view.
     */
    @FXML
    private void goToSignup() {
        Parent signupView = SceneController.getInstance().loadScene(ViewType.REGISTER);

        if (signupView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(signupView);
        }
    }

    /**
     * @brief Updates the UI to display a specific error message.
     *
     * @param message The error string to be displayed to the user.
     */
    private void displayError(String message) {
        errorLabel.setText(message);
    }
}