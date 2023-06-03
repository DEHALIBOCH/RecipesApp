package com.demoapp.recipesapp.domain.firebase;

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
