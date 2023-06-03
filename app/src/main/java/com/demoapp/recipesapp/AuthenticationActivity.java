package com.demoapp.recipesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.databinding.ActivityAuthenticationBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationActivity extends AppCompatActivity {

    private final static String TAG = "AuthenticationActivity";
    private ActivityAuthenticationBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Экземпляр аутентификации firebase
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.signInWithGoogleButton.setOnClickListener(view -> {
            signInGoogle();
        });
    }

    /**
     * Метод для авторизации через аккаунт Google.
     */
    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startForResultGoogleAuthentication.launch(signInIntent);
    }

    /**
     * Получение результата из Activity.
     */
    private final ActivityResultLauncher<Intent> startForResultGoogleAuthentication =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleResult(task);
                        }
                    });

    /**
     * Метод для обработки результата, полученного из активити авторизации с помощью аккаунта Google.
     *
     * @param task обертка для аккаунта GoogleSignInAccount, т.к. запрос асинхронный.
     */
    private void handleResult(Task<GoogleSignInAccount> task) {
        if (task.isSuccessful()) {
            GoogleSignInAccount account = task.getResult();
            if (account != null) {
                signInUserToFirebase(account);
            }
        } else {
            Log.d(TAG, "Auth error", task.getException());
            Toast.makeText(this, getString(R.string.auth_error), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Метод для авторизации юзера в Firebase с помощью токена аккаунта Google.
     *
     * @param account авторизованный аккаунт с помощью Google.
     */
    private void signInUserToFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Успешная авторизация в файрбейз
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "Auth error", task.getException());
                    Toast.makeText(
                            AuthenticationActivity.this,
                            getString(R.string.auth_error),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}