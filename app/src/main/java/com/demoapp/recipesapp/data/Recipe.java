package com.demoapp.recipesapp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Класс предназначенный для хранения основной информации о рецепте: автор, состав, рецепт, фото и т.д.
 */
public class Recipe {

    private final UUID uuid = UUID.randomUUID();    // Уникальный идентификатор рецепта
    private String title;   // Заголовок
    private int serves; // Кол-во порций
    private int cookTime;   // Время приготовления
    private HashMap<String, Integer> ingredients;    // Ингридиенты
    private String recipe;  // Рецептура(процесс приготовления)
    private String authorUID; // Идентификатор автора
    private String firebaseKey; // Ключ в файрбейзе
    private final ArrayList<String> imagesUrlArray = new ArrayList<>();  // Аррейлист для хранения адресов картинок загруженных в БД.

    private String imageUrl;    // Url изображения в файрбейзе
    private String category;

    public Recipe(String authorUID) {
        this.authorUID = authorUID;
    }

    /**
     * Добавление адреса картинки рецепта в базе данных
     *
     * @param imageUrl Url картинки в базе данных
     */
    public void addImageUrl(String imageUrl) {
        this.imagesUrlArray.add(imageUrl);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServes(int serves) {
        this.serves = serves;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public int getServes() {
        return serves;
    }

    public int getCookTime() {
        return cookTime;
    }

    public HashMap<String, Integer> getIngredients() {
        return ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public ArrayList<String> getImagesUrlArray() {
        return imagesUrlArray;
    }

    public String getCategory() {
        return category;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIngredients(HashMap<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", serves=" + serves +
                ", cookTime=" + cookTime +
                ", ingredients=" + ingredients.toString() +
                ", recipe='" + recipe + '\'' +
                ", authorUID='" + authorUID + '\'' +
                ", firebaseKey='" + firebaseKey + '\'' +
                ", imagesUrlArray=" + imagesUrlArray +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
