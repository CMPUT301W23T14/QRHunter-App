package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import com.example.qrhunter.data.model.Comment;
import com.example.qrhunter.data.model.Player;

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
        // Create a player for test
        Player player = new Player("player123", "John");
        // Create test comment with player
        Comment comment = new Comment(player, "Great game!");
        // Test to see if the comment's author is the same as the one set
        assertEquals(player, comment.getAuthor());
    }

    /**
     * Test the getContent method of the Comment class
     * by creating a comment and verifying that the
     * content returned by the getContent method is
     * the same as the content passed in the constructor.
     */
    @Test
    public void testGetContent() {
        // Create a player for test
        Player player = new Player("player123", "John");
        // Create test comment with player
        Comment comment = new Comment(player, "Great game!");
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
        // Create a player for test
        Player player = new Player("player123", "John");
        // Create test comment with player
        Comment comment = new Comment(player, "Great game!");
        // Set content for comment
        comment.setContent("I enjoyed it a lot!");
        // Test to see if the comment's content is the same as the one set
        assertEquals("I enjoyed it a lot!", comment.getContent());
    }
}

