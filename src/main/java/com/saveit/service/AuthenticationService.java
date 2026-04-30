package com.saveit.service;

import com.saveit.dao.UserDAO;
import com.saveit.model.User;

public class AuthenticationService {

    private UserDAO userDAO;
    private static User currentUser;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String rawPassword) {
        User user = userDAO.findByUsername(username);

        if (user != null) {
            String hashedInput = user.hashPassword(rawPassword);

            if (hashedInput.equals(user.getPassword())) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public boolean signUp(String name, String phone, String username, String password, int pin) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setUname(username);
        newUser.setPassword(password);
        newUser.setPin(pin);
        return userDAO.register(newUser);
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
