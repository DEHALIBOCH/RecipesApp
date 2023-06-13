package com.demoapp.recipesapp.domain.firebase;

import com.demoapp.recipesapp.data.Recipe;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Интерфейс представляющий из себя коллбек для взаимодействия с базой данных.
 */
public interface RecipesCallback {

    public void successful(ArrayList<Recipe> list);

    public void unsuccessful(DatabaseError error);

}
