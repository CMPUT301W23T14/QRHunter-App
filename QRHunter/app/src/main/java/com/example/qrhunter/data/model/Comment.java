package com.example.qrhunter.data.model;

/**
 * Model class for a comment.
 * A comment consists of an author (a Player object) and a content string.
 * This class provides methods to retrieve and modify the author and content of the comment.
 */
public class Comment {
    private final Player author;
    private String content;

    /**
     * Creates a new Comment object with the specified author and content.
     *
     * @param author The player who wrote the comment.
     * @param content The text of the comment.
     */
    public Comment(Player author, String content) {
        this.author = author;
        this.content = content;
    }

    /**
     * Returns the author of the comment.
     *
     * @return The player who wrote the comment.
     */
    public Player getAuthor() {
        return author;
    }

    /**
     * Returns the content of the comment.
     *
     * @return The text of the comment.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the comment to the specified string.
     *
     * @param content The new text of the comment.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
