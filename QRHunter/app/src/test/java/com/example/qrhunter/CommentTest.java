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
        Player player = new Player("player123", "John");
        Comment comment = new Comment(player, "Great game!");
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
        Player player = new Player("player123", "John");
        Comment comment = new Comment(player, "Great game!");
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
        Player player = new Player("player123", "John");
        Comment comment = new Comment(player, "Great game!");
        comment.setContent("I enjoyed it a lot!");
        assertEquals("I enjoyed it a lot!", comment.getContent());
    }
}

