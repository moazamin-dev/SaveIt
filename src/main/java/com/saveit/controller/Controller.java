package com.saveit.controller;

import com.saveit.model.User;
import javafx.scene.Node;

public abstract class Controller {

    private User user;

    public abstract void initialize();

    public abstract Node getViewNodes();

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
