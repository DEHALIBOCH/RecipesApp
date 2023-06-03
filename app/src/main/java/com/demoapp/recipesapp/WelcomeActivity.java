package com.demoapp.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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