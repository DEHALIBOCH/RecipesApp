package com.demoapp.recipesapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.RecipeViewModel;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.databinding.FragmentAllRecipesBinding;
import com.demoapp.recipesapp.fragments.recyclerutils.homefragment.RecipesAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class AllRecipesFragment extends Fragment {

    private FragmentAllRecipesBinding binding;
    private RecipeViewModel viewModel;
    private RecipesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllRecipesBinding.inflate(inflater, container, false);

        binding.loadingProgressBar.getRoot().setVisibility(View.VISIBLE);

        initRecyclerView(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ((MainActivity) getActivity()).recipeViewModel;
        viewModel.recipesList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> list) {
                recipesReady(list);
            }
        });
    }

    private void initRecyclerView(Context context) {
        adapter = new RecipesAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.trendingRecipesRecyclerView.setLayoutManager(layoutManager);
        binding.trendingRecipesRecyclerView.setAdapter(adapter);
    }

    private void recipesReady(ArrayList<Recipe> list) {
        binding.loadingProgressBar.getRoot().setVisibility(View.GONE);
        Collections.shuffle(list);
        adapter.setRecipesList(list);
    }
}