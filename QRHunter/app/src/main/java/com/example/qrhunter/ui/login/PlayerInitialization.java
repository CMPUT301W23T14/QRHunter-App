package com.example.qrhunter.ui.login;

import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.UserRepository;
import com.example.qrhunter.databinding.FragmentPlayerInitializationBinding;
import com.example.qrhunter.ui.profile.ProfileFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class PlayerInitialization extends Fragment {

    private FragmentPlayerInitializationBinding binding;
    private String deviceId;
    private String usernameInput;

    private FirebaseFirestore db;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        PlayerInitializationViewModel playerInitializationViewModel = new ViewModelProvider(this).get(PlayerInitializationViewModel.class);
        binding = FragmentPlayerInitializationBinding.inflate(inflater, container, false);

        deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        db = FirebaseFirestore.getInstance();

        //Get username from database
        UserRepository userRepository = new UserRepository();
        db.collection("users").document(deviceId).get().addOnSuccessListener(
                documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        binding.username.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Add delete text button
                            }

                            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Add delete text button
                                usernameInput = s.toString();
                                if (usernameInput.length() > 0) {
                                    binding.button.setTextColor(getResources().getColor(R.color.white));
                                    binding.button.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                                    binding.button.setEnabled(true);
                                } else {
                                    binding.button.setTextColor(getResources().getColor(R.color.primary_dark));
                                    binding.button.setBackgroundColor(getResources().getColor(R.color.primary_lighter));
                                    binding.button.setEnabled(false);
                                }
                                if (userRepository.doesUsernameExist(usernameInput)) {
                                    binding.button.setTextColor(getResources().getColor(R.color.primary_dark));
                                    binding.button.setBackgroundColor(getResources().getColor(R.color.primary_lighter));
                                    binding.button.setEnabled(false);
                                    binding.username.setBackground(getResources().getDrawable(R.drawable.error_field));
                                    binding.supportingText.setText(R.string.error_supporting_text);
                                    binding.supportingText.setTextColor(getResources().getColor(R.color.error));
                                    binding.imageButton2.setBackground(getResources().getDrawable(R.drawable.error));
                                }
                                else if(!userRepository.doesUsernameExist(usernameInput)){
                                    binding.button.setTextColor(getResources().getColor(R.color.white));
                                    binding.button.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                                    binding.button.setEnabled(true);
                                    binding.username.setBackground(getResources().getDrawable(R.drawable.username_text_field));
                                    binding.supportingText.setText(R.string.supporting_text);
                                    binding.supportingText.setTextColor(getResources().getColor(R.color.primary_dark));
                                    binding.imageButton2.setBackground(getResources().getDrawable(R.drawable.cancel_icon));
                                    binding.button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            playerInitializationViewModel.initializePlayer(usernameInput.trim(), deviceId);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // Add delete text button

                            }
                        });
                    }
                    else {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();
                    }
                }
        );





        return binding.getRoot();

    }
}