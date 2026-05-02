package com.saveit.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SideBarController extends Controller {

    @FXML private Label usernameLabel;
    @FXML private ImageView profileImage;

    @Override
    public void initialize() {
        String name = getUser().getName();
        usernameLabel.setText(name);
    }

    @Override
    public Node getViewNodes() {
        // TODO: implement
        return null;
    }

    public void navBtnClicked(String viewName) {
        try {
            ViewType targetView = ViewType.valueOf(viewName.toUpperCase());

            Parent nextView = SceneController.getInstance().loadScene(targetView);

            if (nextView != null) {
                javafx.stage.Stage stage = (javafx.stage.Stage) usernameLabel.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(nextView));
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid View Name: " + viewName);
        }
    }

    @FXML
    private void handleLogout() {
        Parent login = SceneController.getInstance().loadScene(ViewType.LOGIN);
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.setScene(new Scene(login));
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

        String viewName = clickedBtn.getId().replace("Btn", "").toUpperCase();
        navBtnClicked(viewName);
    }
}
