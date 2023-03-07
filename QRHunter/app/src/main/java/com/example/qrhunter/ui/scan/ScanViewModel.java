package com.example.qrhunter.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ScanViewModel extends ViewModel {
    private final MutableLiveData<String> scannedText = new MutableLiveData<>();
    // TODO: Implement the ViewModel
    private FirebaseFirestore db;

    public void scan() {

    }

    public LiveData<String> getScannedText() {
        return scannedText;
    }

    public void storeResult(String text) {
        // store result in firebase
        db = FirebaseFirestore.getInstance();
        // result is stored in a collection called scanned_items_test and a document called test
        CollectionReference usersRef = db.collection("scanned_items_test"); // this is just a temporary collection
        HashMap<String, String> data = new HashMap<>();
        data.put("Scanned item(test)", text); // also temp document name
        usersRef.document("test").set(data);
        scannedText.setValue(text);

    }
}