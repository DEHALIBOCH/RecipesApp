package com.demoapp.recipesapp.fragments.recyclerutils.homefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.databinding.RecipesRcItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes;

    public void setRecipesList(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        this.notifyDataSetChanged();
    }

    public RecipesAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipesRcItemBinding binding = RecipesRcItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currRecipe = recipes.get(position);
        Context context = holder.itemView.getContext();
        String recipeOverview = context.getString(
                R.string.recipe_overview,
                currRecipe.getIngredients().size(),
                currRecipe.getCookTime(),
                currRecipe.getCategory()
        );

        Picasso.get()
                .load(currRecipe.getImageUrl())
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.loading_error)
                .centerCrop()
                .into(holder.binding.recipeTitleImageView);

        holder.binding.recipeTitleTextView.setText(currRecipe.getTitle());
        holder.binding.recipeOverviewTextView.setText(recipeOverview);
        // TODO Bookmarks!!!
        holder.binding.recipeBookmarkCheckBox.setOnClickListener(view -> {

        });
        // TODO RecipeDetailsActivity
        holder.binding.getRoot().setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecipesRcItemBinding binding;

        public ViewHolder(RecipesRcItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
