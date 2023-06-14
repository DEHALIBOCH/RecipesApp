package com.demoapp.recipesapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demoapp.recipesapp.EditProfileActivity;
import com.demoapp.recipesapp.MainActivity;
import com.demoapp.recipesapp.R;
import com.demoapp.recipesapp.RecipeViewModel;
import com.demoapp.recipesapp.WelcomeActivity;
import com.demoapp.recipesapp.data.Recipe;
import com.demoapp.recipesapp.data.User;
import com.demoapp.recipesapp.databinding.FragmentProfileBinding;
import com.demoapp.recipesapp.domain.firebase.FirebaseUtils;
import com.demoapp.recipesapp.domain.firebase.RecipesCallback;
import com.demoapp.recipesapp.domain.firebase.UserCallback;
import com.demoapp.recipesapp.fragments.recyclerutils.homefragment.RecipesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private RecipeViewModel viewModel;
    private FirebaseAuth firebaseAuth;
    private User currUser;
    private FirebaseUtils firebaseUtils;

    private ActivityResultLauncher<Intent> startForResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        viewModel = ((MainActivity) getActivity()).recipeViewModel;
        currUser = viewModel.user;
        // TODO Переделать все, запрашивать пользователя и только потом давать доступ к ui

        firebaseUtils = new FirebaseUtils();
        firebaseAuth = FirebaseAuth.getInstance();

        binding.logoutButton.setOnClickListener(view -> {
            createAlertDialog(requireContext());
        });

        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        User u = (User) intent.getSerializableExtra("user");
                        currUser = u;
                        viewModel.user = u;
                        updateUserInfo(currUser);
                    }
                }
        );

        binding.editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            intent.putExtra("currUser", currUser);
            startForResult.launch(intent);
        });

        showProgressBar();

        getCurrentUser(firebaseAuth.getUid());

        return binding.getRoot();
    }

    private void getCurrentUser(String uid) {
        firebaseUtils.getUserByUID(uid, new UserCallback() {
            @Override
            public void userReady(User user) {
                currUser = user;
                viewModel.user = user;
                updateUserInfo(currUser);
                getUserRecipes(currUser.getTokenUID());
                hideProgressBar();
            }

            @Override
            public void unsuccessful() {
                Context context = requireContext();
                Toast.makeText(context, context.getString(R.string.database_error), Toast.LENGTH_SHORT).show();
                hideProgressBar();
                binding.editProfileButton.setEnabled(false);
            }
        });
    }

    private void setEnabledForAllElements(boolean isEnabled) {
        binding.avatarImageView.setEnabled(isEnabled);
        binding.editProfileButton.setEnabled(isEnabled);
        binding.logoutButton.setEnabled(isEnabled);
        binding.userRecipesRecyclerView.setEnabled(isEnabled);
    }

    private void showProgressBar() {
        setEnabledForAllElements(false);
        binding.loadingProgressBar.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        setEnabledForAllElements(true);
        binding.loadingProgressBar.getRoot().setVisibility(View.GONE);
    }

    /**
     * Создает AlertDialog
     *
     * @param context
     */
    private void createAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.are_sure_to_logout);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> signOut());
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Производит выход из аккаунта
     */
    private void signOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(requireContext(), WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getUserRecipes(String userUid) {
        firebaseUtils.getUserRecipes(userUid, new RecipesCallback() {
            @Override
            public void successful(ArrayList<Recipe> list) {
                if (!list.isEmpty()) {
                    initRecyclerView(list);
                }
            }

            @Override
            public void unsuccessful(DatabaseError error) {

            }
        });
    }

    private void initRecyclerView(ArrayList<Recipe> list) {
        RecipesAdapter recipesAdapter = new RecipesAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.userRecipesRecyclerView.setAdapter(recipesAdapter);
        binding.userRecipesRecyclerView.setLayoutManager(layoutManager);
    }

    private void updateUserInfo(User user) {
        if (user.getAvatarUrl() != null) {
            Picasso.get()
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.loading_placeholder)
                    .error(R.drawable.loading_error)
                    .into(binding.avatarImageView);
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (user.getName() != null) {
            stringBuilder.append(user.getName());
        }
        if (user.getLastname() != null) {
            stringBuilder.append(" ").append(user.getLastname());
        }
        if (!stringBuilder.toString().isBlank()) {
            binding.nameTextView.setText(stringBuilder.toString());
        }
        if (user.getBio() != null) {
            binding.bioTextView.setText(user.getBio());
        }
    }
}