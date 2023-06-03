package com.demoapp.recipesapp.domain.firebase;

import androidx.annotation.NonNull;

import com.demoapp.recipesapp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils {

    private FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private DatabaseReference users = firebaseRepository.getUSERS();

    /**
     * Данный метод необходимо использовать только при авторизации.
     * Метод проверяет, существует ли о пользователе запись в ветке usersбазы данных.
     * Если записи нет, то он создает запись о данном аккаунте.
     *
     * @param currentUser      Юзер который только что авторизовался
     * @param firebaseCallback Коллбек необходимый для обработки успешного и не успешного сценариев
     */
    public void isCurrentUserAlreadyInDatabase(User currentUser, FirebaseCallback firebaseCallback) {
        Query query = users.orderByChild("tokenUID").equalTo(currentUser.getTokenUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    DatabaseReference push = users.push();
                    push.setValue(currentUser);
                }
                firebaseCallback.successful();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                firebaseCallback.unsuccessful();
            }
        });
    }
}
