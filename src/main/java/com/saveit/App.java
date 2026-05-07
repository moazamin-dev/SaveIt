package com.saveit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @brief The main entry point for the SaveIt JavaFX application.
 *
 * This class extends the JavaFX Application class and is responsible for
 * bootstrapping the user interface, loading the initial login view,
 * setting the application icon, and configuring the primary stage.
 */
public class App extends Application {

    /**
     * @brief Starts the primary stage and initializes the main scene.
     *
     * This method loads the login FXML resource, sets up the application
     * window properties (title, icon, and maximized state), and displays
     * the stage to the user.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception If the FXML resource cannot be loaded or initialized.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/saveit/view/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        Image icon = new Image("logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("SaveIt");
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @brief The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}