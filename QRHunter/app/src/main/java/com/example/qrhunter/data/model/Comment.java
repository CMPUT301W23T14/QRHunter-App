package com.example.qrhunter.data.model;

/**
 * Model class for a comment.
 */
public class Comment {
    /**
     * The name of the author of the comment
     */
    private final String author;
    private String content;

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
