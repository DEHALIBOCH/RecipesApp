package com.demoapp.recipesapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.demoapp.recipesapp.databinding.ActivityMainBinding;
import com.demoapp.recipesapp.fragments.AddRecipeFragment;
import com.demoapp.recipesapp.fragments.BookmarksFragment;
import com.demoapp.recipesapp.fragments.HomeFragment;
import com.demoapp.recipesapp.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // TODO - начать работу над MainActivity
        // TODO - посмотреть как хранить файлы в файрбейз
        final int CONTAINER_ID = binding.fragmentContainerView.getId();

        fragmentManager = getSupportFragmentManager();

        final HomeFragment homeFragment = new HomeFragment();
        final BookmarksFragment bookmarksFragment = new BookmarksFragment();
        final AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        fragmentManager.beginTransaction()
                .replace(CONTAINER_ID, homeFragment)
                .commit();

        binding.bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navbar_home) {
                    fragmentManager.beginTransaction()
                            .replace(CONTAINER_ID, homeFragment).commit();
                } else if (itemId == R.id.navbar_bookmarks) {
                    fragmentManager.beginTransaction()
                            .replace(CONTAINER_ID, bookmarksFragment).commit();
                } else if (itemId == R.id.navbar_add_recipe) {
                    fragmentManager.beginTransaction()
                            .replace(CONTAINER_ID, addRecipeFragment).commit();
                } else if (itemId == R.id.navbar_profile) {
                    fragmentManager.beginTransaction()
                            .replace(CONTAINER_ID, profileFragment).commit();
                }

                return true;
            }
        });
    }
}