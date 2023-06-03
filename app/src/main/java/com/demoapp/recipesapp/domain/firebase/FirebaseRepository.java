package com.demoapp.recipesapp.domain.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {

    final static String dataBaseUrl = "https://recipesapp-3020b-default-rtdb.europe-west1.firebasedatabase.app/";
    private final FirebaseDatabase DATABASE;
    private final DatabaseReference USERS;
    private static final FirebaseRepository INSTANCE = new FirebaseRepository();

    private FirebaseRepository() {
        DATABASE = FirebaseDatabase.getInstance(dataBaseUrl);
        USERS = DATABASE.getReference().child("Users");
    }

    /**
     * Метод возвращающий ссылку на ветку Users базы данных.
     *
     * @return Ссылка на ветку Users.
     */
    public DatabaseReference getUSERS() {
        return USERS;
    }

    /**
     * Метод возвращающий ссылку на базу данных.
     *
     * @return Ссылка на базу данных.
     */
    public FirebaseDatabase getDATABASE() {
        return DATABASE;
    }

    /**
     * Метод возвращающий синглтон FirebaseRepository, служащий обэектом доступа к базе данных.
     *
     * @return Возвращает экземпляр FirebaseRepository
     */
    public static FirebaseRepository getInstance() {
        return INSTANCE;
    }
}
