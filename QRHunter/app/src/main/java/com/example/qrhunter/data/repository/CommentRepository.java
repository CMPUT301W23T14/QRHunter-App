package com.example.qrhunter.data.repository;


import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.utils.CommentUtil;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A repository class for containing any data access and business logic related to Comments
 */
public class CommentRepository extends DataRepository {

    /**
     * Get a list of comments given an array of comment ids.
     *
     * @param commentIds         The list of comment ids
     * @param repositoryCallback Returns a list of comment in the callback
     */
    public void getComments(ArrayList<String> commentIds, RepositoryCallback<ArrayList<Comment>> repositoryCallback) {
        if (commentIds.isEmpty())
            return;

        Query commentQuery = db.collection("comments")
                .whereIn(FieldPath.documentId(), commentIds);

        commentQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                ArrayList<Comment> comments = new ArrayList<>();
                for (QueryDocumentSnapshot commentDocument : task.getResult()) {
                    comments.add(CommentUtil.convertDocumentToComment(commentDocument));
                }

                repositoryCallback.onSuccess(comments);
            }
        });
    }

    /**
     * Adds a comment to Firestore
     *
     * @param qrCodeId           The QR Code id the comment is commented on
     * @param comment            The comment object to be added
     * @param repositoryCallback Returns the id of the comment in Firestore in the callback
     */
    public void addComment(String qrCodeId, Comment comment, RepositoryCallback<String> repositoryCallback) {
        Map<String, Object> commentHashmap = CommentUtil.convertCommentToHashmap(comment);

        db.collection("comments").add(commentHashmap).addOnSuccessListener(documentReference -> {
            repositoryCallback.onSuccess(documentReference.getId());
        });
    }
}
