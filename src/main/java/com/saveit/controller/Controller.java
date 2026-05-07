package com.saveit.controller;

import com.saveit.model.User;

/**
 * @brief Base abstract class for all UI controllers in the application.
 *
 * This class provides a common foundation for controllers, ensuring they have
 * access to the currently authenticated User and a standardized initialization
 * mechanism.
 */
public abstract class Controller {

    /** @var User user The authenticated user associated with the current controller session */
    private User user;

    /**
     * @brief Abstract method to be implemented by subclasses for view initialization.
     *
     * This method is typically called after the FXML has been loaded or when
     * the view needs to be refreshed.
     */
    public abstract void initialize();

    /**
     * @brief Sets the user for this controller.
     *
     * @param user The User object representing the currently logged-in user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @brief Retrieves the user associated with this controller.
     *
     * @return User The current user instance.
     */
    public User getUser() {
        return user;
    }
}