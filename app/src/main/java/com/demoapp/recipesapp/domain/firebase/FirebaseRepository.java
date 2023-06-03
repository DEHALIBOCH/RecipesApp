package com.demoapp.recipesapp.domain.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {

    final static String dataBaseUrl= "https://recipesapp-3020b-default-rtdb.europe-west1.firebasedatabase.app/";

    FirebaseDatabase database = FirebaseDatabase.getInstance(dataBaseUrl);
    DatabaseReference users = database.getReference().child("Users");


}
