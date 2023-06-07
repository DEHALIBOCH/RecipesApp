package com.demoapp.recipesapp.data;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Информационный блок(пост), который содержит информацию о рецепте, оценке(рейтинг), времени создания и т.д.
 */
public class RecipePost {

    private final UUID uuid = UUID.randomUUID();    // Id поста
    private final String recipeId;    // Id рецепта
    private final String authorId;    // Id автора
    private final ArrayList<Double> recipeRating = new ArrayList<>();  // рейтинг рецепта
    // TODO рассмотреть возможность комментирования

    public RecipePost(String recipeId, String authorId) {
        this.recipeId = recipeId;
        this.authorId = authorId;
    }

    public void addNewRating(double rating) {
        recipeRating.add(rating);
    }

    public void removeRating(double rating) {
        recipeRating.remove(rating);
    }

    public UUID getId() {
        return uuid;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public ArrayList<Double> getRecipeRating() {
        return recipeRating;
    }
}
