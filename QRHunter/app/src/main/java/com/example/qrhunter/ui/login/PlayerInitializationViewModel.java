package com.example.qrhunter.ui.login;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlayerInitializationViewModel extends ViewModel {

    private Map<String, String> user = new HashMap<>();
    private FirebaseFirestore db;

    public void initializePlayer(String username, String deviceID){
        db = FirebaseFirestore.getInstance();
        user.put("username", username);
        db.collection("users")
                .document(deviceID)
                .set(user);
    }

}