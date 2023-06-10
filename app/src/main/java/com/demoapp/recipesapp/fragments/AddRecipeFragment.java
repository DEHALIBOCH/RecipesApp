package com.demoapp.recipesapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.databinding.FragmentAddRecipeBinding;
import com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment.IngredientsAdapter;


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

        binding.saveMyRecipeButton.setOnClickListener(view -> {
            binding.ingredientsRecyclerView.getAdapter().notifyDataSetChanged();
        });

        binding.addNewIngredientToRecycler.setOnClickListener(view -> {
            ingredientsAdapter.addEmptyIngredient();
            int pos = ingredientsAdapter.getItemCount() - 1;
            ingredientsAdapter.notifyItemInserted(pos);
        });

        return binding.getRoot();
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