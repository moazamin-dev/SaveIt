package com.saveit.controller;

import com.saveit.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController extends Controller {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    private AuthenticationService authService;

    @Override
    public void initialize() {
        authService = new AuthenticationService();
    }

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

    public void backToLogin() {
        Parent loginView = SceneController.getInstance().loadScene(ViewType.LOGIN);

        if (loginView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(loginView);
        }
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }
}
