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
        Player player = new Player("player123", "John");
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
        Player player = new Player("player123", "John", "123-456-7890", 3, 500);
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
        Player player = new Player("player123", "John");
        player.setUsername("Johnny");
        assertEquals("Johnny", player.getUsername());
        player.setPhoneNumber("987-654-3210");
        assertEquals("987-654-3210", player.getPhoneNumber());
        player.setRank(2);
        assertEquals(2, player.getRank());
        player.setTotalScore(1000.0);
        assertEquals(1000.0, player.getTotalScore(), 0.001);
    }
}
