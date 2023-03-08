package com.example.qrhunter.data.model;

/**
 * Model class for a comment.
 */
public class Comment {
    private final Player author;
    private String content;

    public Comment(Player author, String content) {
        this.author = author;
        this.content = content;
    }

    public Player getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
