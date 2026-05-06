package com.saveit.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SideBarController extends Controller {

    @FXML private Label usernameLabel;
    @Override
    public void initialize() {
        String name = getUser().getName();
        usernameLabel.setText(name);
    }

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

    @FXML
    private void handleLogout() {
        Parent login = SceneController.getInstance().loadScene(ViewType.LOGIN);
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.getScene().setRoot(login);
    }

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
