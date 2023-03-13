package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import com.example.qrhunter.data.model.Location;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class QRCodeTest {

    @Test
    public void testConstructor() {
        String id = "1234";
        String hash = "abcdefg";
        ArrayList<String> photos = new ArrayList<>();
        photos.add("1224");
        Location location = new Location(37.7749, -122.4194, photos);
        ArrayList<String> commentIds = new ArrayList<>();
        ArrayList<String> playerIds = new ArrayList<>();

        QRCode qrCode = new QRCode(id, hash, location, commentIds, playerIds);

        assertEquals(id, qrCode.getId());
        assertEquals(hash, qrCode.getHash());
        assertEquals(QRCodeUtil.generateName(hash), qrCode.getName());
        assertEquals(QRCodeUtil.generateVisualRepresentation(hash), qrCode.getVisualRepresentation());
        assertEquals(QRCodeUtil.generateScore(hash), qrCode.getScore(), 0.001);
        assertEquals(location, qrCode.getLocation());
        assertEquals(commentIds, qrCode.getCommentIds());
        assertEquals(playerIds, qrCode.getPlayerIds());
    }

    @Test
    public void testSetters() {
        ArrayList<String> photos = new ArrayList<>();
        photos.add("1224");
        QRCode qrCode = new QRCode("1234", "abcdefg", new Location(37.7749, -122.4194, photos), new ArrayList<>(), new ArrayList<>());

        qrCode.setId("5678");
        assertEquals("5678", qrCode.getId());

        qrCode.setHash("hijklmn");
        assertEquals("hijklmn", qrCode.getHash());

        qrCode.setName("New Name");
        assertEquals("New Name", qrCode.getName());

        qrCode.setVisualRepresentation("New Visual Representation");
        assertEquals("New Visual Representation", qrCode.getVisualRepresentation());

        qrCode.setScore(10.0);
        assertEquals(10.0, qrCode.getScore(), 0.001);

        qrCode.setLocation(new Location(37.7749, -122.4194, photos));

        ArrayList<String> newCommentIds = new ArrayList<>();
        newCommentIds.add("comment1");
        qrCode.setCommentIds(newCommentIds);
        assertEquals(newCommentIds, qrCode.getCommentIds());

        ArrayList<String> newPlayerIds = new ArrayList<>();
        newPlayerIds.add("player1");
        qrCode.setPlayerIds(newPlayerIds);
        assertEquals(newPlayerIds, qrCode.getPlayerIds());
    }
}

