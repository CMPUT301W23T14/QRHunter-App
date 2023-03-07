package com.example.qrhunter.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DataRepository {
    protected static FirebaseFirestore db;
    
    public DataRepository() {
        db = FirebaseFirestore.getInstance();
    }
}
