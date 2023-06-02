package com.demoapp.recipesapp;

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

    }

}