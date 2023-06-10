package com.demoapp.recipesapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.FragmentAddRecipeBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment.IngredientsAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    private IngredientsAdapter ingredientsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);

        initIngredientsRecyclerView(requireContext());

        FirebaseUtils firebaseUtils = new FirebaseUtils();

        binding.saveMyRecipeButton.setOnClickListener(view -> {
            binding.ingredientsRecyclerView.getAdapter().notifyDataSetChanged();
            Recipe recipe = createRecipe();
            Log.d("RECIPE", recipe.toString());
        });

        binding.addNewIngredientToRecycler.setOnClickListener(view -> {
            ingredientsAdapter.addEmptyIngredient();
            int pos = ingredientsAdapter.getItemCount() - 1;
            ingredientsAdapter.notifyItemInserted(pos);
        });

        return binding.getRoot();
    }

    /**
     * Проверяет поля ввода
     */
    private boolean checkInputs() {
        boolean flag = true;

        if (binding.recipeTitleEditText.getText().toString().isEmpty()) {
            binding.recipeTitleEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.servesCountEditText.getText().toString().isEmpty()) {
            binding.servesCountEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.cookTimeCountEditText.getText().toString().isEmpty()) {
            binding.cookTimeCountEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.recipeProcessEditText.getText().toString().isEmpty()) {
            binding.recipeProcessEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }

        return flag;
    }

    /**
     * Метод считывающий поля и создающий рецепт
     *
     * @return объект рецепта
     */
    private Recipe createRecipe() {
        if (!checkInputs()) {
            return null;
        }
        if (MainActivity.currentUser == null) {
            return null;
        }
        User user = MainActivity.currentUser;
        Recipe recipe = new Recipe(user.getTokenUID());
        String title = binding.recipeTitleEditText.getText().toString();
        int servesCount = Integer.parseInt(binding.servesCountEditText.getText().toString());
        int cookTime = Integer.parseInt(binding.cookTimeCountEditText.getText().toString());
        String recipeProcess = binding.recipeProcessEditText.getText().toString();
        HashMap<String, Integer> ingredients = getIngredientsHashMap();
        String category = binding.recipeCategorySpinner.getSelectedItem().toString();
        recipe.setTitle(title);
        recipe.setCategory(category);
        recipe.setServes(servesCount);
        recipe.setCookTime(cookTime);
        recipe.setRecipe(recipeProcess);
        recipe.setIngredients(ingredients);

        return recipe;
    }

    /**
     * Метод возвращающий аасоциативный массив представляющий ингредиенты.
     *
     * @return Map где key - название ингредиента, value - кол-во.
     */
    private HashMap<String, Integer> getIngredientsHashMap() {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ArrayList<String> names = ingredientsAdapter.ingredientNames;
        ArrayList<Integer> quantity = ingredientsAdapter.ingredientQuantities;
        for (int i = 0; i < names.size(); i++) {
            ingredients.put(names.get(i), quantity.get(i));
        }
        return ingredients;
    }

    private void initIngredientsRecyclerView(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.addEmptyIngredient();
        ingredientsAdapter.addEmptyIngredient();
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

}