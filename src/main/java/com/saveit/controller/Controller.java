package com.budget.controller;

import com.budget.model.User;
import javafx.scene.Node;

public abstract class Controller {

    private User user;

    public abstract void initialize();

    public abstract Node getViewNodes();

    public void setUser(User user) {
        // TODO: implement
    }
}
