package com.saveit.controller;

import com.saveit.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief Singleton class responsible for managing scene transitions and controller lifecycle.
 *
 * The SceneController handles the loading of FXML files, instantiation of controllers via a
 * custom controller factory, and global event dispatching (such as user session updates)
 * across all active controllers.
 */
public class SceneController {

    /** @var Map<ViewType, Controller> controllers A registry of instantiated controllers mapped by their view type */
    private final Map<ViewType, Controller> controllers = new HashMap<>();

    /** @var SceneController instance The single static instance of the SceneController */
    private static SceneController instance;

    /**
     * @brief Private constructor to enforce the Singleton pattern.
     */
    private SceneController() {}

    /**
     * @brief Retrieves the Singleton instance of the SceneController.
     * @return SceneController The active instance.
     */
    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    /**
     * @brief Dispatches events to all registered controllers.
     *
     * Currently supports "USER_UPDATED" events to synchronize the active User object
     * across all UI components and re-initialize their logic.
     *
     * @param event The string identifier of the event.
     * @param data The data object associated with the event (e.g., a User instance).
     */
    public void handleEvent(String event, Object data) {
        for (Controller controller : controllers.values()) {


            if (event.equals("USER_UPDATED")) {
                controller.setUser((User) data);
                controller.initialize();
            }
        }
    }

    /**
     * @brief Loads an FXML scene and configures its controller.
     *
     * This method utilizes a custom controller factory to inject the current session
     * user into newly created controllers and registers them in the internal tracking map.
     *
     * @param viewType The ViewType enum indicating which FXML file to load.
     * @return Parent The root node of the loaded scene, or null if loading fails.
     */
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