package com.demoapp.recipesapp.fragments.recyclerutils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.databinding.RecipeIngredientItemBinding;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    public LinkedHashMap<String, Integer> ingredients;

    public RecipeIngredientsAdapter(LinkedHashMap<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecipeIngredientItemBinding binding = RecipeIngredientItemBinding.inflate(
                layoutInflater,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AtomicInteger n = new AtomicInteger();
        ingredients.forEach((key, value) -> {
            if (n.get() == position) {
                holder.binding.ingredientName.setText(key);
                holder.binding.ingredientsCount
                        .setText(holder.itemView.getContext().getString(R.string.ingredient_count, value));
            } else {
                n.getAndIncrement();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecipeIngredientItemBinding binding;

        public ViewHolder(RecipeIngredientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
