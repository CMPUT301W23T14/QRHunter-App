package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.utils.QRCodeUtil;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class QRCodeTest {

    /**
     * Tests the QRCode constructor by creating a new QRCode object and checking that its properties are set correctly.
     */
    @Test
    public void testConstructor() {
        // Create testers
        String id = "1234";
        String hash = "abcdefg";
        ArrayList<String> photos = new ArrayList<>();
        photos.add("1224");
        ArrayList<Location> locations = new ArrayList<>();
        Location location = new Location(37.7749, -122.4194);
        locations.add(location);
        ArrayList<String> commentIds = new ArrayList<>();
        ArrayList<String> playerIds = new ArrayList<>();

        // Create a new QRcode with assigned values
        QRCode qrCode = new QRCode(id, hash, locations, photos, commentIds, playerIds);

        // Test getId
        assertEquals(id, qrCode.getId());
        // Test getHash
        assertEquals(hash, qrCode.getHash());
        // Test getName
        assertEquals(QRCodeUtil.generateName(hash), qrCode.getName());
        // Test getVisualRepresentation
        assertEquals(QRCodeUtil.generateVisualRepresentation(hash), qrCode.getVisualRepresentation());
        // Test getScore
        assertEquals(QRCodeUtil.generateScore(hash), qrCode.getScore(), 0.001);
        // Test getLocations
        assertEquals(locations, qrCode.getLocations());
        // Test getCommentIds
        assertEquals(commentIds, qrCode.getCommentIds());
        // Test getPlayerIds
        assertEquals(playerIds, qrCode.getPlayerIds());
    }

    /**
     * Tests the setter methods of the QRCode class by setting each property to a new value and checking that it has been updated correctly.
     */
    @Test
    public void testSetters() {
        // Create qrCode and ArrayList for qrCode
        ArrayList<String> photos = new ArrayList<>();
        QRCode qrCode = new QRCode("1234", "abcdefg", new ArrayList<Location>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // test setId and see if expected result
        qrCode.setId("5678");
        assertEquals("5678", qrCode.getId());

        // test setHash and see if expected result
        qrCode.setHash("hijklmn");
        assertEquals("hijklmn", qrCode.getHash());

        // test setName and see if expected result
        qrCode.setName("New Name");
        assertEquals("New Name", qrCode.getName());

        // test setVisualRepresentation and see if expected result
        qrCode.setVisualRepresentation("New Visual Representation");
        assertEquals("New Visual Representation", qrCode.getVisualRepresentation());

        // test setScore and see if expected result
        qrCode.setScore(10.0);
        assertEquals(10.0, qrCode.getScore(), 0.001);

        // test setLocation and see if expected result
        qrCode.setLocations(new ArrayList<>());

        ArrayList<String> newCommentIds = new ArrayList<>();
        newCommentIds.add("comment1");

        // test setCommentIds and see if expected result
        qrCode.setCommentIds(newCommentIds);
        assertEquals(newCommentIds, qrCode.getCommentIds());

        ArrayList<String> newPlayerIds = new ArrayList<>();
        newPlayerIds.add("player1");
        // test setPlayerIds and see if expected result
        qrCode.setPlayerIds(newPlayerIds);
        assertEquals(newPlayerIds, qrCode.getPlayerIds());
    }
}

