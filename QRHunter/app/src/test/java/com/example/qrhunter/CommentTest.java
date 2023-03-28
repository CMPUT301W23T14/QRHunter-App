package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import com.example.qrhunter.data.model.Comment;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Comment class
 */
public class CommentTest {

    /**
     * Test the getAuthor method of the Comment class
     * by creating a comment and verifying that the
     * author returned by the getAuthor method is
     * the same as the author passed in the constructor.
     */
    @Test
    public void testGetAuthor() {
        // Create test comment
        Comment comment = new Comment("John", "Great game!");
        // Test to see if the comment's author is the same as the one set
        assertEquals("John", comment.getAuthor());
    }

    /**
     * Test the getContent method of the Comment class
     * by creating a comment and verifying that the
     * content returned by the getContent method is
     * the same as the content passed in the constructor.
     */
    @Test
    public void testGetContent() {
        // Create test comment
        Comment comment = new Comment("John", "Great game!");
        // Test to see if the comment's content is the same as the one set
        assertEquals("Great game!", comment.getContent());
    }

    /**
     * Test the setContent method of the Comment class
     * by creating a comment, setting the content using
     * the setContent method, and verifying that the
     * content returned by the getContent method is
     * the same as the content set using the setContent method.
     */
    @Test
    public void testSetContent() {
        // Create test comment
        Comment comment = new Comment("John", "Great game!");
        // Set content for comment
        comment.setContent("I enjoyed it a lot!");
        // Test to see if the comment's content is the same as the one set
        assertEquals("I enjoyed it a lot!", comment.getContent());
    }
}

