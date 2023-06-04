package com.demoapp.recipesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        // TODO - Не забыть убрать(сейчас необходима так как нет возможности выйти из аккаунта)
        firebaseAuth.signOut();

        binding.buttonStartCooking.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserAuth();
    }

    /**
     * Метод получает текущего авторизованного юзера.
     * Если текущий юзер не равен null, значет пользователь авторизован и кидаем его сразу в
     * MainActivity.
     */
    private void checkUserAuth() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}