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
    private Map<String, Object> user = new HashMap<>();

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
            // Test case for adding someone to database
            user.put("first", "Ada");
            user.put("last", "Lovelace");
            user.put("born", 1815);

            // Add a new document with a generated ID
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        });

        profileViewModel.getCount().observe(getViewLifecycleOwner(), newInteger -> binding.counter.setText(String.valueOf(newInteger)));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}