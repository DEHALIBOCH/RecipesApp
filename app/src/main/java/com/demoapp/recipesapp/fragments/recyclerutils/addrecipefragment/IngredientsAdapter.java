package com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.databinding.IngredientsRcViewItemBinding;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    public ArrayList<String> ingredientNames = new ArrayList<>();
    public ArrayList<Integer> ingredientQuantities = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngredientsRcViewItemBinding binding = IngredientsRcViewItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.removeIngredientButton.setOnClickListener(view -> {
            removeIngredient(holder.getAdapterPosition());
        });
        try {
            String name = holder.binding.itemNameEditText.toString();
            ingredientNames.remove(position);
            ingredientNames.add(position, name);
        } catch (Exception ignored) {
        }
        try {
            String quantityStr = holder.binding.quantityEditText.toString();
            Integer quantity = Integer.parseInt(quantityStr);
            ingredientQuantities.remove(position);
            ingredientQuantities.add(position, quantity);
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        return ingredientNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        IngredientsRcViewItemBinding binding;

        public ViewHolder(IngredientsRcViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Добавляет пустые поля для ингредиентов
     */
    public void addEmptyIngredient() {
        ingredientNames.add("");
        ingredientQuantities.add(-1);
        notifyItemInserted(ingredientNames.size() - 1);
    }

    /**
     * Удаляет ингредиент из RecyclerView на определенной позиции
     *
     * @param position позиция элемента
     */
    private void removeIngredient(int position) {
        ingredientNames.remove(position);
        ingredientQuantities.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }



}
