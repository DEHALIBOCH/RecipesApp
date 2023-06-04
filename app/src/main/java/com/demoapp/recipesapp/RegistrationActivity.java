package com.demoapp.recipesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityRegistrationBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseCallback;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    public static final String EMAIL_EXTRAS = "email_extras";
    public static final String PASSWORD_EXTRAS = "password_extras";
    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.alreadyRegisteredTextView.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        binding.signUpButton.setOnClickListener(view -> registerUser());
    }

    /**
     * Регистрация пользователя.
     */
    private void registerUser() {
        if (isValidUserInput()) {
            String email = String.valueOf(binding.emailEditTextRegister.getText()).trim();
            String password = String.valueOf(binding.passwdEditTextRegister.getText()).trim();
            showProgressBar();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Uid - уникальный идентификатор пользователя.
                        String userUid = task.getResult().getUser().getUid();
                        User user = new User(email, password, userUid);
                        addUserToDB(user);
                    } else {
                        removeProgressBar();
                        Toast.makeText(RegistrationActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Метод проверяет введенные пользователем данные.
     * Если данные не пустые, пароли совпадают и электронный адрес прошел проверку
     * метод возвращает true.
     */
    private boolean isValidUserInput() {
        removeErrors();

        String email = String.valueOf(binding.emailEditTextRegister.getText()).trim();
        String password = String.valueOf(binding.passwdEditTextRegister.getText()).trim();
        String confirmPassword = String.valueOf(binding.confirmPasswdEditTextRegister.getText()).trim();

        boolean isNotEmpty = true;

        if (email.isBlank()) {
            isNotEmpty = false;
            binding.emailLayout.setError(getString(R.string.empty_input_field));
        }
        if (password.isBlank()) {
            isNotEmpty = false;
            binding.passwordLayout.setError(getString(R.string.empty_input_field));
        }
        if (confirmPassword.isBlank()) {
            isNotEmpty = false;
            binding.confirmPasswordLayout.setError(getString(R.string.empty_input_field));
        }
        if (!isNotEmpty) {
            return false;
        }

        if (!validEmail(email)) {
            binding.emailLayout.setError(getString(R.string.not_valid_email));
            return false;
        }

        if (password.length() < 6) {
            binding.passwordLayout.setError(getString(R.string.not_valid_password));
            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.passwordLayout.setError(getString(R.string.passwords_dont_match));
            return false;
        }

        return true;
    }

    private void removeErrors() {
        binding.emailLayout.setError(null);
        binding.passwordLayout.setError(null);
        binding.confirmPasswordLayout.setError(null);
    }

    /**
     * Проверяет на соответсвие введеный пользователем email
     *
     * @param email электронный адресс введенный пользователем
     * @return возвращает истину если эл.адрес содержит символы перед '@', символы после '@',
     * точку после символов и минимум 2 сиввола после точки.
     */
    private boolean validEmail(String email) {
        Pattern valid_email_address_regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,9}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = valid_email_address_regex.matcher(email);
        return matcher.matches();
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
     * @param isEnabled устанавливает булевское значение определяющее включено ли взаимодействие
     */
    private void setEnabledForAllUiElements(boolean isEnabled) {
        binding.emailLayout.setEnabled(isEnabled);
        binding.passwordLayout.setEnabled(isEnabled);
        binding.signUpButton.setEnabled(isEnabled);
        binding.confirmPasswordLayout.setEnabled(isEnabled);
        binding.alreadyRegisteredTextView.setEnabled(isEnabled);
    }

    /**
     * Добавляет пользователя в базу данных, в случае успеха закрывает активити и передает информацию
     * для автоизации в AuthenticationActivity для удобной авторизации.
     *
     * @param currUser текущий зарегистрировавшийся пользователь
     */
    private void addUserToDB(User currUser) {
        FirebaseUtils firebaseUtils = new FirebaseUtils();
        firebaseUtils.addUserToDatabase(currUser, new FirebaseCallback() {
            @Override
            public void successful() {
                removeProgressBar();
                Toast.makeText(RegistrationActivity.this, getString(R.string.successful_registration), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(EMAIL_EXTRAS, currUser.getEmail());
                intent.putExtra(PASSWORD_EXTRAS, currUser.getPassword());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void unsuccessful() {
                removeProgressBar();
                Toast.makeText(RegistrationActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}