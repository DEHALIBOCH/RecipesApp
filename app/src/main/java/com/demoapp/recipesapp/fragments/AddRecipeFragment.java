package com.demoapp.recipesapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.FragmentAddRecipeBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseCallback;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.fragments.recyclerutils.addrecipefragment.IngredientsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    private IngredientsAdapter ingredientsAdapter;
    private StorageReference storageReference;
    private FirebaseUtils firebaseUtils;
    private boolean ifImageLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);

        initIngredientsRecyclerView(requireContext());

        firebaseUtils = new FirebaseUtils();

        binding.saveMyRecipeButton.setOnClickListener(view -> {
            binding.ingredientsRecyclerView.getAdapter().notifyDataSetChanged();
            Recipe recipe = createRecipe();
            uploadRecipeToFirebase(recipe);

        });

        binding.addNewIngredientToRecycler.setOnClickListener(view -> {
            ingredientsAdapter.addEmptyIngredient();
            int pos = ingredientsAdapter.getItemCount() - 1;
            ingredientsAdapter.notifyItemInserted(pos);
        });

        binding.imagePickImageView.setOnClickListener(view -> {
            if (checkPhotoPermissions()) selectImage();
        });

        return binding.getRoot();
    }

    /**
     * После успешной загрузки картинки в firebase, загружает рецепт в базуданных
     *
     * @param recipe
     */
    private void uploadRecipeToFirebase(Recipe recipe) {
        binding.progressBar.setVisibility(View.VISIBLE);
        setEnabledForAllElements(false);
        uploadImage(recipe, new FirebaseCallback() {
            @Override
            public void successful() {
                firebaseUtils.addRecipeToDatabase(recipe);
                binding.progressBar.setVisibility(View.GONE);
                setEnabledForAllElements(true);
                cleanDataFromFragment();
            }

            @Override
            public void unsuccessful() {
                Toast.makeText(requireContext(), getString(R.string.database_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Очищает поля ввода после успешной загрузки рецепта
     */
    public void cleanDataFromFragment() {
        binding.imagePickImageView.setImageResource(R.drawable.baseline_add_photo_alternate_24);
        binding.recipeTitleEditText.setText("");
        binding.servesCountEditText.setText("");
        binding.cookTimeCountEditText.setText("");
        binding.recipeCategorySpinner.setSelection(0);
        initIngredientsRecyclerView(requireContext());
        binding.recipeProcessEditText.setText("");
    }


    /**
     * Устанавливает возможность нажатия на элементы, чтобы во время загрузки убрать с элементов
     * возможность нажатия
     *
     * @param isEnabled доступны ли элементы
     */
    private void setEnabledForAllElements(boolean isEnabled) {
        binding.imagePickImageView.setEnabled(isEnabled);
        binding.recipeTitleEditText.setEnabled(isEnabled);
        binding.servesCountEditText.setEnabled(isEnabled);
        binding.cookTimeCountEditText.setEnabled(isEnabled);
        binding.recipeCategorySpinner.setEnabled(isEnabled);
        binding.addNewIngredientToRecycler.setEnabled(isEnabled);
        binding.ingredientsRecyclerView.setEnabled(isEnabled);
        binding.recipeProcessEditText.setEnabled(isEnabled);
        binding.saveMyRecipeButton.setEnabled(isEnabled);
    }

    /**
     * Лаунчер для получения пермишеннов от пользователя
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(requireContext(), getString(R.string.no_permissions), Toast.LENGTH_SHORT).show();
                }
            });

    /**
     * Проверяет пермишены которые даны пользователем
     *
     * @return true - если все разрешения выданы
     */
    private boolean checkPhotoPermissions() {
        boolean flag = true;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            flag = false;
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            flag = false;
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return flag;
    }

    /**
     * Выбирает картинку
     */
    private void selectImage() {
        startActivityForContent.launch("image/*");
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
                                        requireContext().getContentResolver(),
                                        result
                                );
                                binding.imagePickImageView.setImageBitmap(bitmap);
                                binding.imagePickImageView.setTag(result);
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), "Error with image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    /**
     * Загружает картинку в firebase storage
     */
    private void uploadImage(Recipe recipe, FirebaseCallback firebaseCallback) {
//        Bitmap bitmap = ((BitmapDrawable) binding.imagePickImageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
//        byte[] bytes = baos.toByteArray();
//        StorageReference stRef = storageReference.child(recipe.getAuthorUID());
//        UploadTask uploadTask = stRef.putBytes(bytes);
//        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return storageReference.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                Uri uri = task.getResult();
//                recipe.setImageUrl(uri);
//                firebaseCallback.successful();
//            }
//        });

        String location = "images/" + recipe.getAuthorUID() + "/" + recipe.getUniqueId();

        storageReference = FirebaseStorage.getInstance().getReference(location);

        Uri uri = (Uri) binding.imagePickImageView.getTag();
        Context context = requireContext();

        if (uri == null) {
            firebaseCallback.successful();
            return;
        }

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(context, context.getString(R.string.img_upload_success), Toast.LENGTH_SHORT).show();
                                recipe.setImageUrl(uri.toString());
                                firebaseCallback.successful();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, context.getString(R.string.img_upload_failure), Toast.LENGTH_SHORT).show();
                        firebaseCallback.unsuccessful();
                    }
                });
    }

    /**
     * Проверяет поля ввода
     */
    private boolean checkInputs() {
        boolean flag = true;

        if (binding.recipeTitleEditText.getText().toString().isEmpty()) {
            binding.recipeTitleEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.servesCountEditText.getText().toString().isEmpty()) {
            binding.servesCountEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.cookTimeCountEditText.getText().toString().isEmpty()) {
            binding.cookTimeCountEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }
        if (binding.recipeProcessEditText.getText().toString().isEmpty()) {
            binding.recipeProcessEditText.setError(getString(R.string.invalid_data));
            flag = false;
        }

        return flag;
    }

    /**
     * Метод считывающий поля и создающий рецепт
     *
     * @return объект рецепта
     */
    private Recipe createRecipe() {
        if (!checkInputs()) {
            return null;
        }
        if (MainActivity.currentUser == null) {
            return null;
        }
        User user = MainActivity.currentUser;
        Recipe recipe = new Recipe(user.getTokenUID());
        String title = binding.recipeTitleEditText.getText().toString();
        int servesCount = Integer.parseInt(binding.servesCountEditText.getText().toString());
        int cookTime = Integer.parseInt(binding.cookTimeCountEditText.getText().toString());
        String recipeProcess = binding.recipeProcessEditText.getText().toString();
        HashMap<String, Integer> ingredients = getIngredientsHashMap();
        String category = binding.recipeCategorySpinner.getSelectedItem().toString();
        recipe.setTitle(title);
        recipe.setCategory(category);
        recipe.setServes(servesCount);
        recipe.setCookTime(cookTime);
        recipe.setRecipe(recipeProcess);
        recipe.setIngredients(ingredients);

        return recipe;
    }

    /**
     * Метод возвращающий аасоциативный массив представляющий ингредиенты.
     *
     * @return Map где key - название ингредиента, value - кол-во.
     */
    private HashMap<String, Integer> getIngredientsHashMap() {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ArrayList<String> names = ingredientsAdapter.ingredientNames;
        ArrayList<Integer> quantity = ingredientsAdapter.ingredientQuantities;
        for (int i = 0; i < names.size(); i++) {
            ingredients.put(names.get(i), quantity.get(i));
        }
        return ingredients;
    }

    private void initIngredientsRecyclerView(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsAdapter.addEmptyIngredient();
        ingredientsAdapter.addEmptyIngredient();
        binding.ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

}