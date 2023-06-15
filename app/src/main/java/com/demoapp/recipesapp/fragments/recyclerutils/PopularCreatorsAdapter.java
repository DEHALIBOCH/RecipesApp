package com.demoapp.recipesapp.fragments.recyclerutils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.PopularCreatorsItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularCreatorsAdapter extends RecyclerView.Adapter<PopularCreatorsAdapter.ViewHolder> {

    private ArrayList<User> authors;

    public PopularCreatorsAdapter(ArrayList<User> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PopularCreatorsItemBinding binding = PopularCreatorsItemBinding.inflate(
                layoutInflater,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User author = authors.get(position);
        if (author.getAvatarUrl() != null) {
            Picasso.get()
                    .load(author.getAvatarUrl())
                    .placeholder(R.drawable.loading_placeholder)
                    .error(R.drawable.loading_error)
                    .into(holder.binding.avatarImageView);
        }
        String name = author.getName();
        String lastname = author.getLastname();
        if (name != null) {
            holder.binding.authorName.setText(name);
        }
        if (lastname != null) {
            holder.binding.authorName.setText(lastname);
        }
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        PopularCreatorsItemBinding binding;

        public ViewHolder(PopularCreatorsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Устанавливает лист авторов и уведомляет адаптер о изменении списка
     *
     * @param authors лист авторов
     */
    public void setAuthorsList(ArrayList<User> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }
}
