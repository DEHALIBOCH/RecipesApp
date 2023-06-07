package com.demoapp.recipesapp.fragments.recyclerutils.homefragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.databinding.TrendingNowRecipesRcItemBinding;

import java.util.ArrayList;

public class TrendingNowRecipes extends RecyclerView.Adapter<TrendingNowRecipes.ViewHolder> {

    public ArrayList<Recipe> recipes;

    public TrendingNowRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrendingNowRecipesRcItemBinding binding = TrendingNowRecipesRcItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO доделать
        Recipe currRecipe = recipes.get(position);
        holder.binding.recipeTitleTextView.setText("");
        holder.binding.recipeAuthorTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TrendingNowRecipesRcItemBinding binding;

        public ViewHolder(TrendingNowRecipesRcItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
