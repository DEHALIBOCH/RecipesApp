package com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment;

import android.text.Editable;
import android.text.TextWatcher;
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
        int pos = position;
        holder.binding.itemNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientNames.remove(pos);
                ingredientNames.add(pos, s.toString());
            }
        });

        holder.binding.quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientQuantities.remove(pos);
                ingredientQuantities.add(pos, Integer.parseInt(s.toString()));
            }
        });
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
