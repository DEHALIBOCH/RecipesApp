package com.demoapp.recipesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityEditProfileBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseCallback;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    private User currUser;
    private StorageReference storageReference;
    private FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUtils = new FirebaseUtils();

        currUser = (User) getIntent().getSerializableExtra("currUser");

        setUsersData(currUser);

        binding.avatarImageView.setOnClickListener(view -> {
            if (checkPhotoPermissions()) selectImage();
        });

        binding.commitButton.setOnClickListener(view -> {
            if (checkUserInputs()) {
                String name = binding.nameEditText.getText().toString();
                String lastname = binding.lastnameEditText.getText().toString();
                String bio = binding.bioEditText.getText().toString();
                int age = Integer.parseInt(binding.ageEditText.getText().toString());

                currUser.setName(name);
                currUser.setLastname(lastname);
                currUser.setBio(bio);
                currUser.setAge(age);

                updateUser(currUser);
                Constants.USER = currUser;
            }
        });
    }

    /**
     * Обновляет текущего пользователя в базе данных
     *
     * @param currUser
     */
    private void updateUser(User currUser) {
        binding.progressBar.setVisibility(View.VISIBLE);
        setEnabledForAllElements(false);
        uploadImage(currUser, new FirebaseCallback() {
            @Override
            public void successful() {
                firebaseUtils.updateUser(currUser, new FirebaseCallback() {
                    @Override
                    public void successful() {
                        binding.progressBar.setVisibility(View.GONE);
                        setEnabledForAllElements(true);
                        Toast.makeText(EditProfileActivity.this, getString(R.string.successful_update), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra("user", currUser);
                        finish();
                    }

                    @Override
                    public void unsuccessful() {
                        binding.progressBar.setVisibility(View.GONE);
                        setEnabledForAllElements(true);
                        Toast.makeText(EditProfileActivity.this, getString(R.string.database_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void unsuccessful() {
                binding.progressBar.setVisibility(View.GONE);
                setEnabledForAllElements(true);
                Toast.makeText(EditProfileActivity.this, getString(R.string.img_upload_failure), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setEnabledForAllElements(boolean isEnabled) {
        binding.avatarImageView.setEnabled(isEnabled);
        binding.nameEditText.setEnabled(isEnabled);
        binding.lastnameEditText.setEnabled(isEnabled);
        binding.ageEditText.setEnabled(isEnabled);
        binding.bioEditText.setEnabled(isEnabled);
        binding.commitButton.setEnabled(isEnabled);
    }

    /**
     * Устанавливает существующие данные о пользователе в базе данных
     */
    private void setUsersData(User currUser) {

        binding.ageEditText.setText(String.valueOf(currUser.getAge()));

        if (currUser.getName() != null) {
            binding.nameEditText.setText(currUser.getName());
        }
        if (currUser.getLastname() != null) {
            binding.lastnameEditText.setText(currUser.getLastname());
        }
        if (currUser.getBio() != null) {
            binding.bioEditText.setText(currUser.getBio());
        }
    }

    /**
     * Проверяет введенные данные
     *
     * @return true если все поля не пустые
     */
    private boolean checkUserInputs() {
        boolean flag = true;

        String name = binding.nameEditText.getText().toString();
        String lastname = binding.lastnameEditText.getText().toString();
        String bio = binding.bioEditText.getText().toString();
        String age = binding.ageEditText.getText().toString();

        if (name.isEmpty() || name.isBlank()) {
            binding.nameEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (lastname.isEmpty() || lastname.isBlank()) {
            binding.lastnameEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (bio.isEmpty() || bio.isBlank()) {
            binding.bioEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }

        if (age.isEmpty() || age.isBlank()) {
            binding.ageEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }

        int userAge = 0;
        try {
            userAge = Integer.parseInt(age);
        } catch (Exception e) {
            Log.d("AGE", age + "", e);
        }

        return flag;
    }

    /**
     * Загружает картинку в firebase storage
     */
    private void uploadImage(User user, FirebaseCallback firebaseCallback) {

        String location = "avatars/" + user.getTokenUID();

        storageReference = FirebaseStorage.getInstance().getReference(location);

        Uri uri = (Uri) binding.avatarImageView.getTag();

        if (uri == null) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.add_photo_to_recipe), Toast.LENGTH_SHORT).show();
            firebaseCallback.unsuccessful();
            return;
        }

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(EditProfileActivity.this, getString(R.string.img_upload_success), Toast.LENGTH_SHORT).show();
                                user.setAvatarUrl(uri.toString());
                                firebaseCallback.successful();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, getString(R.string.img_upload_failure), Toast.LENGTH_SHORT).show();
                        firebaseCallback.unsuccessful();
                    }
                });
    }


    /**
     * Запускает лаунчер для выбора фотографии с устройства пользователя
     */
    private final ActivityResultLauncher<String> startActivityForContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri result) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),
                                        result
                                );
                                binding.avatarImageView.setImageBitmap(bitmap);
                                binding.avatarImageView.setTag(result);
                            } catch (Exception e) {
                                Toast.makeText(EditProfileActivity.this, "Error with image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    /**
     * Выбирает картинку
     */
    private void selectImage() {
        startActivityForContent.launch("image/*");
    }

    /**
     * Проверяет пермишены которые даны пользователем
     *
     * @return true - если все разрешения выданы
     */
    private boolean checkPhotoPermissions() {
        boolean flag = true;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            flag = false;
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            flag = false;
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return flag;
    }

    /**
     * Лаунчер для получения пермишеннов от пользователя
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, getString(R.string.no_permissions), Toast.LENGTH_SHORT).show();
                }
            });


}