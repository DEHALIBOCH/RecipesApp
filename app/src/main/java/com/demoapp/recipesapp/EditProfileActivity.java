package com.demoapp.recipesapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.ActivityEditProfileBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseCallback;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    private RecipeViewModel viewModel;
    private User currUser;
    private StorageReference storageReference;
    private FirebaseUtils firebaseUtils;
    private boolean isAvatarSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUtils = new FirebaseUtils();

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        currUser = viewModel.currentUser;

        setUsersData(currUser);

        binding.avatarImageView.setOnClickListener(view -> {
            if (checkPhotoPermissions()) selectImage();
        });

        binding.commitButton.setOnClickListener(view -> {
            if (checkUserInputs()) {
                String name = binding.nameEditText.getText().toString();
                String lastname = binding.lastnameEditText.getText().toString();
                String location = binding.locationEditText.getText().toString();
                int age = age = Integer.parseInt(binding.nameEditText.getText().toString());

                currUser.setName(name);
                currUser.setLastname(lastname);
                currUser.setLocation(location);
                currUser.setAge(age);


                updateUser(currUser);
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
        if (isAvatarSelected) {
            uploadImage(currUser, new FirebaseCallback() {
                @Override
                public void successful() {
                    firebaseUtils.updateUser(currUser, new FirebaseCallback() {
                        @Override
                        public void successful() {
                            binding.progressBar.setVisibility(View.GONE);
                            setEnabledForAllElements(true);

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
    }

    private void setEnabledForAllElements(boolean isEnabled) {
        binding.avatarImageView.setEnabled(isEnabled);
        binding.nameEditText.setEnabled(isEnabled);
        binding.lastnameEditText.setEnabled(isEnabled);
        binding.ageEditText.setEnabled(isEnabled);
        binding.locationEditText.setEnabled(isEnabled);
        binding.commitButton.setEnabled(isEnabled);
    }

    /**
     * Устанавливает существующие данные о пользователе в базе данных
     */
    private void setUsersData(User currUser) {
        binding.ageEditText.setText(currUser.getAge());
        if (currUser.getName() != null) {
            binding.nameEditText.setText(currUser.getName());
        }
        if (currUser.getLastname() != null) {
            binding.lastnameEditText.setText(currUser.getLastname());
        }
        if (currUser.getLocation() != null) {
            binding.locationEditText.setText(currUser.getLocation());
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
        String location = binding.locationEditText.getText().toString();

        if (name.isEmpty() || name.isBlank()) {
            binding.nameEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (lastname.isEmpty() || lastname.isBlank()) {
            binding.lastnameEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (location.isEmpty() || location.isBlank()) {
            binding.locationEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }

        int age = 0;
        try {
            age = Integer.parseInt(binding.nameEditText.getText().toString());
        } catch (Exception e) {
            flag = false;
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
                                isAvatarSelected = true;
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