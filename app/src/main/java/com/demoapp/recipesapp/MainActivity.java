package com.demoapp.recipesapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityMainBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.domain.firebase.UserCallback;
import com.demoapp.recipesapp.fragments.AddRecipeFragment;
import com.demoapp.recipesapp.fragments.BookmarksFragment;
import com.demoapp.recipesapp.fragments.HomeFragment;
import com.demoapp.recipesapp.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    public static User currentUser = null;
    private FirebaseAuth firebaseAuth;
    public RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
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

        String currentUserUid = firebaseAuth.getUid();
        if (currentUserUid != null) {
            FirebaseUtils firebaseUtils = new FirebaseUtils();
            firebaseUtils.getUserByUID(currentUserUid, new UserCallback() {
                @Override
                public void userReady(User user) {
                    currentUser = user;
                    recipeViewModel.currentUser = user;
                }

                @Override
                public void unsuccessful() {
                    Toast.makeText(MainActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}