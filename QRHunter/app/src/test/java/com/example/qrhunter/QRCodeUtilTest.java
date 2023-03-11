package com.example.qrhunter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.qrhunter.utils.QRCodeUtil;

import org.junit.jupiter.api.Test;

public class QRCodeUtilTest {

    @Test
    public void calculatePoints_isCorrect() {
        // Testing case when character is alphabetical character
        assertEquals(11, QRCodeUtil.generatePoints('b', 1));
        // Testing case when character is number
        assertEquals(5, QRCodeUtil.generatePoints('5', 1));
        // Testing math.pow function for alphabetical case
        assertEquals(1331, QRCodeUtil.generatePoints('b', 3));
        // Testing math.pow function for number case
        assertEquals(3125, QRCodeUtil.generatePoints('5', 5));
    }
    @Test
    public void hashQR_isCorrect() {
        // Hash given in example is wrong since echo call does not include -n to remove newline character
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", QRCodeUtil.generateHash("BFG5DGW54"));
    }
    @Test
    public void calculateScore_isCorrect() {
        String qrCode = QRCodeUtil.generateHash("BFG5DGW54");
        // Repeats in hash are 22, 55, 44, 88 which is 2+5+4+8=19 + 4 0's = 23
        assertEquals(23, QRCodeUtil.generateScore(qrCode));
        // test for consistency
        assertEquals(QRCodeUtil.generateScore(qrCode), QRCodeUtil.generateScore(qrCode));
        // Hash given in project description
        assertEquals(115, QRCodeUtil.generateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6"));
    }


    @Test
    public void checkGenerateVisualRepresentation(){
        String hasValue = QRCodeUtil.generateHash("BFG5DGW54");
        // check if the two generation given the same hash is consistent
        String expectedOutput = QRCodeUtil.generateVisualRepresentation(hasValue);
        assertEquals(expectedOutput, QRCodeUtil.generateVisualRepresentation(hasValue));

        String hasValue2 = QRCodeUtil.generateHash("BFG5DGLMA");
        // check if the two generation given the same hash is consistent
        String expectedOutput2 = QRCodeUtil.generateVisualRepresentation(hasValue2);
        assertEquals(expectedOutput2, QRCodeUtil.generateVisualRepresentation(hasValue2));
    }

    @Test
    public void checkGenerateName(){
        String hasValue = QRCodeUtil.generateHash("BFG5DGW54");
        // check if the two generation given the same hash is consistent
        String expectedOutput = QRCodeUtil.generateName(hasValue);
        assertEquals(expectedOutput, QRCodeUtil.generateName(hasValue));

        String hasValue2 = QRCodeUtil.generateHash("BFG5DGLMA");
        // check if the two generation given the same hash is consistent
        String expectedOutput2 = QRCodeUtil.generateName(hasValue2);
        assertEquals(expectedOutput2, QRCodeUtil.generateName(hasValue2));
    }


}
