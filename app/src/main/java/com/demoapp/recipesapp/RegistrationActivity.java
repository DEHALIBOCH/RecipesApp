package com.demoapp.recipesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.demoapp.recipesapp.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}