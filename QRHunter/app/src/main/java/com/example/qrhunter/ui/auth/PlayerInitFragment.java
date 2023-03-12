package com.example.qrhunter.ui.auth;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentPlayerInitBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.Disposable;

public class PlayerInitFragment extends Fragment {

    private FragmentPlayerInitBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel, using activities' lifecycle because the state in
        PlayerInitViewModel playerInitViewModel = new ViewModelProvider(getActivity()).get(PlayerInitViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentPlayerInitBinding.inflate(inflater, container, false);

        // Hide navigation bar
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

        // Enable debounce for user name input
        Disposable suscription = RxTextView.textChanges(binding.usernameEditText).debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(newUsername -> {
                    playerInitViewModel.updateUsername(newUsername.toString());
                });

        // Changing styles depending on whether name is valid
        playerInitViewModel.getValidUsername().observe(getViewLifecycleOwner(), validUsername -> {
            if (validUsername) {
                // Set button color
                binding.nextButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primary, null));
                binding.nextButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                // Enable button since its valid
                binding.nextButton.setEnabled(true);

                // Clear any errors
                binding.usernameTextField.setError("");
            } else {
                // Set button color
                binding.nextButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primary_lighter, null));
                binding.nextButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.primary_dark, null));
                // Disable button since its valid
                binding.nextButton.setEnabled(false);

                // Display error on text field
                binding.usernameTextField.setError("Please enter a unique and non-empty username");
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            playerInitViewModel.createPlayer(deviceId, binding.usernameEditText.getText().toString());
        });

        // Observe the player state in PlayerViewModel, if it exists then we can navigate to the map screen
        playerInitViewModel.getPlayer().observe(getViewLifecycleOwner(), player -> {
            if (!player.getId().isEmpty()) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_playerInitFragment_to_navigation_map);

                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        return binding.getRoot();
    }

}