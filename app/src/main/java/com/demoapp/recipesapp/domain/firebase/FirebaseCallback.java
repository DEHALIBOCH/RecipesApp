package com.demoapp.recipesapp.domain.firebase;

/**
 * Интерфейс представляющий из себя коллбек для взаимодействия с базой данных.
 */
public interface FirebaseCallback {

    /**
     * Коллбэк предназначенный для обработки успешного получения или отправки данных.
     */
    public void successful();

    /**
     * Коллбэк предназначенный для обработки не успешного получения или отправки данных.
     */
    public void unsuccessful();
}
