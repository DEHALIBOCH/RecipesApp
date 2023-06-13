package com.demoapp.recipesapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.RecipeViewModel;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.databinding.FragmentHomeBinding;
import com.demoapp.recipesapp.fragments.recyclerutils.homefragment.RecipesAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecipeViewModel viewModel;
    private RecipesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initRecyclerView(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ((MainActivity) requireActivity()).recipeViewModel;

        viewModel.recipesList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> list) {
                recipesReady(list);
            }
        });
    }

    /**
     * Метод для инициализации RecyclerView
     */
    private void initRecyclerView(Context context) {
        adapter = new RecipesAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.trendingRecipesRecyclerView.setLayoutManager(layoutManager);
        binding.trendingRecipesRecyclerView.setAdapter(adapter);
    }

    /**
     * Метод вызывается когда рецепты пришли с сервера, передает их в адаптер и уведомляет его.
     *
     * @param list Список рецептор
     */
    private void recipesReady(ArrayList<Recipe> list) {
        adapter.setRecipesList(list);
    }
}