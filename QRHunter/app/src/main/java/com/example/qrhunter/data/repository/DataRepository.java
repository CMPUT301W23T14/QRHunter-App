package com.example.qrhunter.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * An abstract class to be extended by any repository classes. Ex: UserRepository, PlayerRepository, etc.
 */
public abstract class DataRepository {
    protected static FirebaseFirestore db;

    public DataRepository() {
        db = FirebaseFirestore.getInstance();
    }
}
