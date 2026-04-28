package com.saveit.controller;

import com.saveit.service.AuthenticationService;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController extends Controller {

    private TextField nameField;
    private TextField usernameField;
    private TextField email;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private AuthenticationService authService;

    @Override
    public void initialize() {
        // TODO: implement
    }

    public void handleRegister() {
        // TODO: implement
    }

    public void backToLogin() {
        // TODO: implement
    }

    @Override
    public Node getViewNodes() {
        // TODO: implement
        return null;
    }

    private boolean validateInput() {
        // TODO: implement
        return false;
    }
}
