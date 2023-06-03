package com.demoapp.recipesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Только для теста auth_activity */
        binding.buttonStartCooking.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        });

    }

}