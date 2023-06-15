package com.demoapp.recipesapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.Constants;
import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.RecipeViewModel;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.FragmentHomeBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.domain.firebase.UserCallback;
import com.demoapp.recipesapp.fragments.recyclerutils.PopularCreatorsAdapter;
import com.demoapp.recipesapp.fragments.recyclerutils.homefragment.RecipesAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecipeViewModel viewModel;
    private RecipesAdapter adapter;
    private PopularCreatorsAdapter authorsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initRecyclerView(requireContext());
        initAuthorsRecyclerView(requireContext());

        binding.seeAllTrendingRecipes.setOnClickListener(view -> {
            AllRecipesFragment allRecipesFragment = new AllRecipesFragment();
            getParentFragmentManager().beginTransaction().replace(
                    R.id.fragment_container_view,
                    allRecipesFragment
            ).addToBackStack("allRecipesFragment").commit();
        });

        return binding.getRoot();
    }

    private void showProgressBar() {
        binding.loadingProgressBar.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.loadingProgressBar.getRoot().setVisibility(View.GONE);
    }

    // TODO протестить


    private void initUser() {
        showProgressBar();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUid = firebaseAuth.getUid();
        if (currentUserUid != null) {
            FirebaseUtils firebaseUtils = new FirebaseUtils();
            firebaseUtils.getUserByUID(currentUserUid, new UserCallback() {
                @Override
                public void userReady(User user) {
                    Constants.USER = user;
                    viewModel.user = user;
                    hideProgressBar();
                }

                @Override
                public void unsuccessful() {
                    Toast.makeText(requireContext(), getString(R.string.database_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ((MainActivity) getActivity()).recipeViewModel;
        initUser();

        viewModel.recipesList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> list) {
                recipesReady(list);
            }
        });

        viewModel.usersList.observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                authorsReady(users);
            }
        });
    }

    private void initAuthorsRecyclerView(Context context) {
        authorsAdapter = new PopularCreatorsAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.popularCreatorsRecyclerView.setLayoutManager(layoutManager);
        binding.popularCreatorsRecyclerView.setAdapter(authorsAdapter);
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
        Collections.shuffle(list);
        adapter.setRecipesList(list);
    }

    /**
     * Метод вызывается когда авторы пришли с сервера, передает их в адаптер и уведомляет его.
     *
     * @param list Список авторов
     */
    private void authorsReady(ArrayList<User> list) {
        Collections.shuffle(list);
        authorsAdapter.setAuthorsList(list);
    }
}