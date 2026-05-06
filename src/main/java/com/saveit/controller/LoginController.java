package com.saveit.controller;

import com.saveit.model.User;
import com.saveit.service.AuthenticationService;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

public class LoginController extends Controller {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    private AuthenticationService authService;

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

    private void openMainDashboard(User user) {
        SceneController.getInstance().handleEvent("USER_UPDATED", user);

        Parent dashboard = SceneController.getInstance().loadScene(ViewType.BUDGET_SETUP);

        if (dashboard != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(dashboard);
        }
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

    @FXML
    private void goToSignup() {
        Parent signupView = SceneController.getInstance().loadScene(ViewType.REGISTER);

        if (signupView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.getScene().setRoot(signupView);
        }
    }

    private void displayError(String message) {
        errorLabel.setText(message);
    }
}
