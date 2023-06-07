package com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.databinding.IngredientsRcViewItemBinding;

import java.util.HashMap;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    public HashMap<String, Integer> ingredients = new HashMap<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngredientsRcViewItemBinding binding = IngredientsRcViewItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false)

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO сделать наблюдатель за названием ингредиента и за кол-вом граммов
        // TODO сделать дизайн для пикера ингредиентов
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        IngredientsRcViewItemBinding binding;


        public ViewHolder(IngredientsRcViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
