package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import com.example.qrhunter.data.model.Player;

/**
 * Unit tests for the Player class
 */
public class PlayerTest {

    /**
     * Test the constructor that takes an id and a username
     */
    @Test
    public void testConstructorWithIdAndUsername() {
        // Create a test player that is new
        Player player = new Player("player123", "John");
        // Test all getters
        assertEquals("player123", player.getId());
        assertEquals("John", player.getUsername());
        assertEquals("", player.getPhoneNumber());
        assertEquals(0, player.getRank());
        assertEquals(0, player.getTotalScore(), 0.001);
    }

    /**
     * Test the constructor that takes all fields
     */
    @Test
    public void testConstructorWithAllFields() {
        // Create a test player that already exists
        Player player = new Player("player123", "John", "123-456-7890", 3, 500);
        // Test all getters
        assertEquals("player123", player.getId());
        assertEquals("John", player.getUsername());
        assertEquals("123-456-7890", player.getPhoneNumber());
        assertEquals(3, player.getRank());
        assertEquals(500.0, player.getTotalScore(), 0.001);
    }

    /**
     * Test the setters for all fields
     */
    @Test
    public void testSetters() {
        // Create a test player
        Player player = new Player("player123", "John");
        // Set username and test
        player.setUsername("Johnny");
        assertEquals("Johnny", player.getUsername());
        // Set phone-number and test
        player.setPhoneNumber("987-654-3210");
        assertEquals("987-654-3210", player.getPhoneNumber());
        // Set rank and test
        player.setRank(2);
        assertEquals(2, player.getRank());
        // Set totalscore and test
        player.setTotalScore(1000.0);
        assertEquals(1000.0, player.getTotalScore(), 0.001);
    }
}
