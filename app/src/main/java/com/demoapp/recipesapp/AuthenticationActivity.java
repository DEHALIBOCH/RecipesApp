package com.demoapp.recipesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityAuthenticationBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseCallback;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationActivity extends AppCompatActivity {

    private final static String TAG = "AuthenticationActivity";
    private ActivityAuthenticationBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Экземпляр аутентификации firebase
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        firebaseUtils = new FirebaseUtils();

        binding.signInWithGoogleButton.setOnClickListener(view -> {
            signInGoogle();
        });

        binding.signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(AuthenticationActivity.this, RegistrationActivity.class);
            startActivity(intent);
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
    private final ActivityResultLauncher<Intent> startForResultGoogleAuthentication = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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
                signInUserToFirebaseWithGoogle(account);
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
    private void signInUserToFirebaseWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        String email = account.getEmail();
        showProgressBar();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {  // Успешная авторизация в файрбейз
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    User user = new User(email, uid);
                    isUserAlreadyExistsInDB(user);
                } else {
                    Log.d(TAG, "Auth error", task.getException());
                    Toast.makeText(AuthenticationActivity.this, getString(R.string.auth_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isUserAlreadyExistsInDB(User currentUser) {
        firebaseUtils.isCurrentUserAlreadyInDatabase(currentUser, new FirebaseCallback() {
            @Override
            public void successful() {
                successfulAuth();
            }

            @Override
            public void unsuccessful() {
                removeProgressBar();
                Toast.makeText(AuthenticationActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Показывает progressBar и убирает возможность взаимодействия с элементами пользовательского
     * интерфейса
     */
    private void showProgressBar() {
        binding.getRoot().setForeground(getDrawable(R.drawable.auth_foreground));
        setEnabledForAllUiElements(false);
        binding.authProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Убирает progressBar и возвращает возможность взаимодействия с элементами ользовательского
     * интерфейса
     */
    private void removeProgressBar() {
        binding.getRoot().setForeground(null);
        setEnabledForAllUiElements(true);
        binding.authProgressBar.setVisibility(View.GONE);
    }

    /**
     * Включает и выключает взаимодействие с элементами экрана
     *
     * @param isEnabled
     */
    private void setEnabledForAllUiElements(boolean isEnabled) {
        binding.emailLayout.setEnabled(isEnabled);
        binding.passwordLayout.setEnabled(isEnabled);
        binding.signInButton.setEnabled(isEnabled);
        binding.signInWithGoogleButton.setEnabled(isEnabled);
        binding.signUpTextView.setEnabled(isEnabled);
    }

    private void signInWithEmailAndPassword() {
        boolean flag = true;
        String email = String.valueOf(binding.emailEditTextAuth.getText());
        String password = String.valueOf(binding.passwdEditTextAuth.getText());

        if (email.isBlank()) {
            flag = false;
            binding.emailEditTextAuth.setError(getString(R.string.empty_input_field));
        }
        if (password.isBlank()) {
            flag = false;
            binding.passwdEditTextAuth.setError(getString(R.string.empty_input_field));
        }

        if (!flag) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    successfulAuth();
                } else {
                    removeProgressBar();
                    Toast.makeText(AuthenticationActivity.this, getString(R.string.auth_fail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void successfulAuth() {
        removeProgressBar();
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        // Убирает все предидущие активити из бэкстека
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}