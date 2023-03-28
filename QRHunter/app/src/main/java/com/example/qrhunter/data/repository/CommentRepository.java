package com.example.qrhunter.data.repository;


import com.google.firebase.firestore.FieldPath;

import java.util.ArrayList;

/**
 * A repository class for containing any data access and business logic related to Comments
 */
public class CommentRepository extends DataRepository {
    public void getComments(ArrayList<String> commentIds) {
        db.collection("comments").whereIn(FieldPath.documentId(),)
    }
}
