package com.demoapp.recipesapp.domain.firebase;

import com.demoapp.recipesapp.data.User;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public interface AllUsersCallback {

    public void usersReady(ArrayList<User> users);

    public void unsuccessful(DatabaseError error);
}
