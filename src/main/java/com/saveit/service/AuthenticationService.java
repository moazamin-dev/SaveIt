package com.saveit.service;

import com.saveit.dao.UserDAO;
import com.saveit.model.User;

/**
 * @brief Service class handling user authentication and session management.
 *
 * This class provides methods for user login, registration, and logout,
 * while maintaining a reference to the currently authenticated user session.
 */
public class AuthenticationService {

    /** @var UserDAO userDAO Data Access Object for user-related database operations */
    private UserDAO userDAO;

    /** @var User currentUser The static reference to the currently logged-in user */
    private static User currentUser;

    /**
     * @brief Constructor for AuthenticationService.
     *
     * Initializes the UserDAO to enable database interaction.
     */
    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * @brief Authenticates a user based on username and password.
     *
     * Retrieves the stored password hash from the database and compares it with
     * the hash of the provided raw password. If they match, the currentUser is set.
     *
     * @param username The username provided by the user.
     * @param rawPassword The plain-text password provided by the user.
     * @return boolean True if authentication is successful, false otherwise.
     */
    public boolean login(String username, String rawPassword) {
        String storedHash = userDAO.getStoredPassword(username);

        if (storedHash != null) {
            // Note: Uses the hashPassword utility from the User model
            if (User.hashPassword(rawPassword).equals(storedHash)) {
                this.currentUser = userDAO.findByUsername(username);
                return true;
            }
        }
        return false;
    }

    /**
     * @brief Registers a new user in the system.
     *
     * Creates a new User object and passes it along with the plain-text password
     * to the DAO for hashing and storage.
     *
     * @param name The full name of the user.
     * @param phone The contact phone number.
     * @param username The desired unique username.
     * @param password The raw password to be secured and stored.
     * @return boolean True if the sign-up process was successful, false otherwise.
     */
    public boolean signUp(String name, String phone, String username, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setUname(username);
        return userDAO.register(newUser, password);
    }

    /**
     * @brief Logs out the current user by clearing the session reference.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * @brief Retrieves the currently authenticated user.
     * @return User The User object of the current session, or null if no user is logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}