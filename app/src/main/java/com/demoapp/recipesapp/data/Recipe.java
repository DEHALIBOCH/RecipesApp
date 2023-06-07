package com.demoapp.recipesapp.data;

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
    private final HashMap<String, Double> ingredients = new HashMap<>();    // Ингридиенты
    private String recipe;  // Рецептура(процесс приготовления)
    private String authorUID; // Идентификатор автора
    private String firebaseKey; // Ключ в файрбейзе
}
