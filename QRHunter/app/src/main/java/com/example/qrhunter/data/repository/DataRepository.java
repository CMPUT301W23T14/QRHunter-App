package com.example.qrhunter.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * An abstract class that serves as the parent class for other data repositories.
 * This class contains a static instance of the FirebaseFirestore class used to interact with Firestore.
 */
public abstract class DataRepository {
    protected static FirebaseFirestore db;
    /**
     * Constructor for the DataRepository class.
     * Initializes the db instance using the getInstance() method of the FirebaseFirestore class.
     */
    public DataRepository() {
        db = FirebaseFirestore.getInstance();
    }
}
