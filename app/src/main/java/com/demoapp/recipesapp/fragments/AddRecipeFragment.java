package com.demoapp.recipesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.databinding.FragmentAddRecipeBinding;


public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}