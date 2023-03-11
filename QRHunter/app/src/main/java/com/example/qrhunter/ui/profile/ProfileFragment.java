package com.example.qrhunter.ui.profile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.addButton.setOnClickListener(view -> {
            profileViewModel.addCount();

            FirebaseFirestore db;
            // Get a reference to the database
            db = FirebaseFirestore.getInstance();
            // Test case for adding a person's username, scores, and QRCodes
            profileViewModel.addScore(5000);
            profileViewModel.addScore(1000);
            profileViewModel.addScore(200);
            profileViewModel.addQR("test_hash");
            profileViewModel.addDocument();
        });

        profileViewModel.getCount().observe(getViewLifecycleOwner(), newInteger -> binding.counter.setText("Number of Documents:" + String.valueOf(newInteger)));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}