package com.utez.objetosperdidos.util;

import com.utez.objetosperdidos.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Session {
    public static ObservableList<User> users = FXCollections.observableArrayList();
    public static User currentUser = null;
    
    static {

        users.add(new User("Admin", "Mauro@utez.edu", "12345", "77711223344", "20233tn117","admin"));
    }

}
