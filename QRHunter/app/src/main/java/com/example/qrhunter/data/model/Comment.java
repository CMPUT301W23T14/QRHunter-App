package com.example.qrhunter.data.model;

/**
 * Model class for a comment.
 */
public class Comment {
    private final String authorId;
    private String content;

    public Comment(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
