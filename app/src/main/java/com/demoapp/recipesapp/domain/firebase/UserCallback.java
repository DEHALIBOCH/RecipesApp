package com.demoapp.recipesapp.domain.firebase;

import com.demoapp.recipesapp.data.User;

/**
 * Интерфейс-коллбек для получения юзера
 */
public interface UserCallback {
    public void userReady(User user);
    public void unsuccessful();
}
