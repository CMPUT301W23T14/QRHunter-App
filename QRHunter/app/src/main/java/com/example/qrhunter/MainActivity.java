package com.example.qrhunter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.qrhunter.data.repository.UserRepository;
import com.example.qrhunter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    UserRepository userRepository;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        // Get instance of User Repository
        userRepository = new UserRepository();

        // Get device id and check whether the player exists or not
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        userRepository.doesPlayerExist(deviceId, result -> {
            if (!result) {
                navController.navigate(R.id.playerInitFragment);
            }
        });
    }
}