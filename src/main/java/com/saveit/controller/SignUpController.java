package com.saveit.controller;

import com.saveit.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @brief Controller responsible for managing user registration (Sign Up).
 *
 * This class handles the logic for creating a new user account. it validates
 * that all fields are filled, checks if the password and confirmation password match,
 * and communicates with the AuthenticationService to persist the new user data.
 */
public class SignUpController extends Controller {

    /** @var TextField nameField Input field for the user's full name */
    @FXML private TextField nameField;

    /** @var TextField usernameField Input field for the desired username */
    @FXML private TextField usernameField;

    /** @var TextField phoneField Input field for the user's phone number */
    @FXML private TextField phoneField;

    /** @var PasswordField passwordField Input field for the new password */
    @FXML private PasswordField passwordField;

    /** @var PasswordField confirmPasswordField Input field to confirm the new password */
    @FXML private PasswordField confirmPasswordField;

    /** @var Label errorLabel Label used to display validation or registration errors to the user */
    @FXML private Label errorLabel;

    /** @var AuthenticationService authService Service handling the backend registration logic */
    private AuthenticationService authService;

    /**
     * @brief Initializes the controller by instantiating the AuthenticationService.
     *
     * This method is automatically called after the FXML file has been loaded.
     */
    @Override
    public void initialize() {
        authService = new AuthenticationService();
    }

    /**
     * @brief Processes the registration form data.
     *
     * Validates that all fields are populated and that passwords match.
     * If validation passes, it attempts to register the user via authService.
     * On success, it redirects to the login screen; otherwise, it displays an error.
     */
    public void handleRegister() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            displayError("Passwords do not match.");
            return;
        }
        if (name.isEmpty() || username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            displayError("Please fill in all fields.");
            return;
        }
        if (!authService.signUp(name, phone, username, password)) {
            displayError("Username exists.");
        }
        else{
            backToLogin();
        }
    }

    /**
     * @brief Navigates the UI back to the Login view.
     *
     * This is called either manually by the user or automatically after a successful
     * registration.
     */
    public void backToLogin() {
        Parent loginView = SceneController.getInstance().loadScene(ViewType.LOGIN);

        if (loginView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(loginView);
        }
    }

    /**
     * @brief Updates the UI error label with a specific message.
     *
     * @param message The error message to display.
     */
    private void displayError(String message) {
        errorLabel.setText(message);
    }
}