package com.demoapp.recipesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}