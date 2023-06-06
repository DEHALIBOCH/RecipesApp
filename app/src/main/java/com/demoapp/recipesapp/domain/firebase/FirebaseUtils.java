package com.demoapp.recipesapp.domain.firebase;

import androidx.annotation.NonNull;

import com.demoapp.recipesapp.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils {

    private final FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
    private final DatabaseReference users = firebaseRepository.getUSERS();

    /**
     * Данный метод необходимо использовать только при авторизации.
     * Метод проверяет, существует ли о пользователе запись в ветке users базы данных.
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
                    String firebaseKey = push.getKey();
                    currentUser.setFirebaseKey(firebaseKey);
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

    /**
     * Метод добавляет нового пользователя в базу данных.
     *
     * @param currentUser пользователь
     */
    public void addUserToDatabase(User currentUser, FirebaseCallback firebaseCallback) {
        DatabaseReference push = users.push();
        String firebaseKey = push.getKey();
        currentUser.setFirebaseKey(firebaseKey);
        push.setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseCallback.successful();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.unsuccessful();
            }
        });
    }

    /**
     * Метод для того чтобы получить пользователя из базы данных
     *
     * @param uid          Идентификатор пользователя которого необходимо получить
     * @param userCallback коллбек для взаимодействия с юзером, когда данные будут готовы
     */
    public void getUserByUID(String uid, UserCallback userCallback) {
        Query query = users.orderByChild("tokenUID").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User receivedUser = document.getValue(User.class);
                    userCallback.userReady(receivedUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userCallback.unsuccessful();
            }
        });
    }

    /**
     * Метод для обновления информации о пользователе.
     *
     * @param currentUser
     * @param firebaseCallback
     */
    public void updateUser(User currentUser, FirebaseCallback firebaseCallback) {
        users.child("tokenUID").setValue(currentUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseCallback.successful();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firebaseCallback.unsuccessful();
                    }
                });

    }
}
