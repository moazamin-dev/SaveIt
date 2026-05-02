package com.saveit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/saveit/view/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        Image icon = new Image("logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("SaveIt");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}