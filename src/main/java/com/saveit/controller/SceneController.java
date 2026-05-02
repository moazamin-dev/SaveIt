package com.saveit.controller;

import com.saveit.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {

    private final Map<ViewType, Controller> controllers = new HashMap<>();
    private static SceneController instance;

    private SceneController() {}

    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void handleEvent(String event, Object data) {
        for (Controller controller : controllers.values()) {


            if (event.equals("USER_UPDATED")) {
                controller.setUser((User) data);
                controller.initialize();
            }
        }
    }

    public Parent loadScene(ViewType viewType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewType.getFxmlFile()));

            loader.setControllerFactory(clazz -> {
                try {
                    Controller controller = (Controller) clazz.getDeclaredConstructor().newInstance();

                    User currentSessionUser = com.saveit.service.AuthenticationService.getCurrentUser();
                    controller.setUser(currentSessionUser);

                    controllers.put(viewType, controller);

                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller with User data", e);
                }
            });

            return loader.load();

        } catch (IOException e) {
            System.err.println("Could not find FXML at: " + viewType.getFxmlFile());
            e.printStackTrace();
            return null;
        }
    }
}
