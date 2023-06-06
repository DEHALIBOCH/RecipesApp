package com.demoapp.recipesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.databinding.ActivityMainBinding;
import com.demoapp.recipesapp.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // TODO - начать работу над MainActivity
        // TODO - посмотреть как хранить файлы в файрбейз

        getSupportFragmentManager().beginTransaction()
                .add(binding.fragmentContainerView.getId(), new HomeFragment())
                .commit();

        /*
        // TODO Убрать - только для тестов базы нужно
        FirebaseUtils firebaseUtils = new FirebaseUtils();
        firebaseUtils.getUserByUID("AzYxQmg9ibVhjMgwFoaVVQz8gpr1", new UserCallback() {
            @Override
            public void userReady(User user) {
                Toast.makeText(
                        MainActivity.this,
                        user.toString(),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("User", user.toString());
            }

            @Override
            public void unsuccessful() {

            }
        });
         */
    }
}