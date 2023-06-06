package com.demoapp.recipesapp.data;

import java.util.ArrayList;

/**
 * Класс для обеспечения логики взаимодействия между пользователями.
 * Пользователь может подписаться на другого пользователя и следить за ним.
 * А так же имеет своих подписчиков.
 */
public class UserRelations {
    private User currentUser;

    public UserRelations(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * При подписке текущего пользователя(A) на другого(B) мы добавляем юзера-A в список подписчиков юзера-B и
     * юзера-B в список подписок юзера-A.
     *
     * @param anotherUser Пользователь на которого мы подписываемся
     */
    public void followToUser(User anotherUser) {
        currentUser.addFollowing(anotherUser);
        anotherUser.addFollower(currentUser);
    }

    /**
     * При отписке текущего пользователя(A) от другого(B) мы удаляем юзера-A из список подписчиков юзера-B и
     * юзера-B из списка подписок юзера-A.
     *
     * @param anotherUser
     */
    public void unfollowToUser(User anotherUser) {
        currentUser.removeFollowing(anotherUser);
        anotherUser.removeFollower(currentUser);
    }
}
