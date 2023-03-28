package com.example.qrhunter.utils;

import com.example.qrhunter.data.model.Comment;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
/**
 * Utility class for Comment objects. This class provides methods for converting DocumentSnapshot objects to Comment objects,
 * and converting Comment objects to HashMaps for adding data to Firestore.
 */
public final class CommentUtil {
    /**
     * Converts A Document Snapshot object to a Comment Object. Used when retrieving data from Firestore
     *
     * @param documentSnapshot The document snapshot to be converted
     * @return A comment object
     */
    public static Comment convertDocumentToComment(DocumentSnapshot documentSnapshot) {
        String author = documentSnapshot.get("author").toString();
        String content = documentSnapshot.get("content").toString();

        Comment comment = new Comment(author, content);

        return comment;
    }

    /**
     * Converts A Comment object to a hash map. Used when adding data to Firestore
     *
     * @param comment The comment to be converted a hashmap
     * @return A hash map
     */
    public static Map<String, Object> convertCommentToHashmap(Comment comment) {
        Map<String, Object> commentHashmap = new HashMap<>();
        commentHashmap.put("author", comment.getAuthor());
        commentHashmap.put("content", comment.getContent());

        return commentHashmap;
    }
}
