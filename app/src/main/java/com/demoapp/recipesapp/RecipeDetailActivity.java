package com.demoapp.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityRecipeDetailBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.domain.firebase.UserCallback;
import com.demoapp.recipesapp.fragments.recyclerutils.RecipeIngredientsAdapter;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;

public class RecipeDetailActivity extends AppCompatActivity {
    private ActivityRecipeDetailBinding binding;
    private Recipe recipe;
    int n = 1;
    private FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("recipe");
        firebaseUtils = new FirebaseUtils();

        getCurrentUser(recipe);
    }

    private void getCurrentUser(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        binding.loadingProgressBar.getRoot().setVisibility(View.VISIBLE);
        firebaseUtils.getUserByUID(recipe.getAuthorUID(), new UserCallback() {
            @Override
            public void userReady(User user) {
                updateUi(recipe, user);
            }

            @Override
            public void unsuccessful() {
                Toast.makeText(RecipeDetailActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi(Recipe currRecipe, User user) {
        if (recipe == null || user == null) {
            return;
        }

        Picasso.get()
                .load(currRecipe.getImageUrl())
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.loading_error)
                .into(binding.recipeImageView);

        Picasso.get()
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.loading_error)
                .into(binding.authorAvatarImageView);

        binding.authorTextView.setText(user.getName() + "" + user.getLastname());
        StringBuilder ingredients = new StringBuilder();
        recipe.getIngredients().forEach((key, value) -> {
            ingredients.append(n).append(") ").append(key).append(" - ").append(value).append("\n");
            n++;
        });
        binding.recipeTitle.setText(recipe.getTitle());
        binding.ingredientsTextView.setText(ingredients.toString());
        binding.recipeTextView.setText(recipe.getRecipe());
        binding.ingredientsCountTextView.setText(getString(R.string.items_count, recipe.getIngredients().size()));
        binding.loadingProgressBar.getRoot().setVisibility(View.GONE);
    }

}