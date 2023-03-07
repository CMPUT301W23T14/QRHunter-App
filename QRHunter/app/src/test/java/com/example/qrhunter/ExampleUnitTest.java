package com.example.qrhunter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.qrhunter.data.repository.QRCodeRepository;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void calculatePoints_isCorrect() {
        QRCodeRepository test = new QRCodeRepository();
        assertEquals(11, test.calculatePoints('b', 1));
        assertEquals(5, test.calculatePoints('5', 1));
        assertEquals(81, test.calculatePoints('9', 2));
        assertEquals(196, test.calculatePoints('e', 2));
        assertEquals(1331, test.calculatePoints('b', 3));
        assertEquals(3125, test.calculatePoints('5', 5));
    }
    @Test
    public void hashQR_isCorrect() {
        QRCodeRepository test = new QRCodeRepository();
        // Hash given in example is wrong since echo call does not include -n to remove newline character
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", test.hashQR("BFG5DGW54"));
    }
    @Test
    public void calculateScore_isCorrect() {
        QRCodeRepository test = new QRCodeRepository();
        String qrCode = test.hashQR("BFG5DGW54");
        // Repeats in hash are 22, 55, 44, 88 which is 2+5+4+8=19 + 4 0's = 23
        assertEquals(23, test.calculateScore(qrCode));
        // Hash given in project description
        assertEquals(115, test.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6"));
    }
}