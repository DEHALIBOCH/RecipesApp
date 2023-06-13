package com.demoapp.recipesapp;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.domain.firebase.RecipesCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * ViewModel для сохранения листа рецептов
 */
public class RecipeViewModel extends ViewModel {

    private final FirebaseUtils firebaseUtils = new FirebaseUtils();
    public MutableLiveData<ArrayList<Recipe>> recipesList = new MutableLiveData<>();
    public User currentUser = null;
    public int start = 0;

    public RecipeViewModel() {
        getRecipes();
    }

    public void getRecipes() {
        firebaseUtils.getAllRecipes(start, new RecipesCallback() {
            @Override
            public void successful(ArrayList<Recipe> list) {
                recipesList.setValue(list);
            }

            @Override
            public void unsuccessful(DatabaseError error) {
                Log.e("RecipeViewModel", error.getDetails());
            }
        });
    }
}