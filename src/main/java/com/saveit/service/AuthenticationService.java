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
        String storedHash = userDAO.getStoredPassword(username);

        if (storedHash != null) {
            if (currentUser.hashPassword(rawPassword).equals(storedHash)) {
                this.currentUser = userDAO.findByUsername(username);
                return true;
            }
        }
        return false;
    }

    public boolean signUp(String name, String phone, String username, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setUname(username);
        return userDAO.register(newUser, password);
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
