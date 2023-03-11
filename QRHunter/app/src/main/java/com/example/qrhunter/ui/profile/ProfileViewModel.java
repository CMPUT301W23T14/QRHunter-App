    package com.example.qrhunter.ui.profile;

    import static android.content.ContentValues.TAG;

    import android.util.Log;

    import androidx.annotation.NonNull;
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;

    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.util.HashMap;
    import java.util.Map;

    public class ProfileViewModel extends ViewModel {
        private final MutableLiveData<Integer> count = new MutableLiveData<>(0);
        private Map<String, Object> user = new HashMap<>();
        private FirebaseFirestore db;
        private Integer score_increment;

        public ProfileViewModel() {
            // You can initialize repository classes here
        }

        public LiveData<Integer> getCount() {
            return count;
        }

        public void addCount() {
            Integer currentValue = count.getValue();
            count.setValue(currentValue + 1);
        }


        public void addScore(Integer score){
            db = FirebaseFirestore.getInstance();
            addCount();
            //String temp_score = score_increment + "";
            Integer currentValue = count.getValue();
            user.put("score" + currentValue, score);

        }

        public void addQR(String hash){
            db = FirebaseFirestore.getInstance();
            user.put("username", hash);
        }

        public void addDocument(){
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
        }
    }