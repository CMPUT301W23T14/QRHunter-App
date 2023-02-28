package com.example.qrhunter.data.repository;

import androidx.annotation.NonNull;

import org.apache.commons.codec.digest.DigestUtils;

public class QRCodeRepository {
    public String hashQR (String qrCode) {
        return DigestUtils.sha256Hex(qrCode);
    }
    public double calculateScore(@NonNull String hash) {
        double score = 0;
        char[] array = hash.toCharArray();
        for (int i = 0; i < array.length-1; i++) {
            int value = 0;
            if (array[i] == array [i+1] ) {
                int j = i +1;
                while (array[i] == array[j]) {
                    value += 1;
                    j += 1;
                    if (j == array.length) {
                        break;
                    }
                }
            }
            if (value > 0) {
                score += calculatePoints(array[i], value);
                i += value;
            }
        }
        return score;
    }

    public double calculatePoints(Character key, Integer value) {
        double points;
        int ascii = (int) key;
        if (ascii < 58) {
            ascii -= 48;
        }
        else {
            ascii -= 87;
        }
        points = Math.pow(ascii, value);
        return points;
    }

}
