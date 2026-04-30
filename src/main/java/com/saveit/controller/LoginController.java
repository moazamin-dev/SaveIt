package com.saveit.controller;

import com.saveit.model.User;
import com.saveit.service.AuthenticationService;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;

public class LoginController extends Controller {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    private AuthenticationService authService;

    public void handleLogin() {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            displayError("Please fill in all fields.");
            return;
        }

        boolean success = authService.login(username, password);

        if (success) {
            System.out.println("Login Successful! Welcome " + AuthenticationService.getCurrentUser().getName());
        } else {
            displayError("Invalid username or password.");
        }
    }

    private void openMainDashboard(User user) {
        // TODO: implement
    }

    @Override
    public void initialize() {
        authService = new AuthenticationService();
    }

    @Override
    public Node getViewNodes() {
        // TODO: implement
        return null;
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }
//    private void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
}
