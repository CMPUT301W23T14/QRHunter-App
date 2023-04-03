package com.example.qrhunter.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;
import com.example.qrhunter.utils.PlayerUtil;
import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The ViewModel for the ProfileFragment that stores and manages data related to the user profile.
 */

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private final MutableLiveData<QRCode> highScoreQRCode = new MutableLiveData<>(new QRCode());
    private final MutableLiveData<QRCode> lowScoreQRCode = new MutableLiveData<>(new QRCode());


    private final MutableLiveData<ArrayList<QRCode>> scannedQRCodes = new MutableLiveData<>(new ArrayList<>());
    private PlayerRepository playerRepository = new PlayerRepository();
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();


    /**
     * Gets a player object
     *
     * @param playerId The id of the player
     * @return A live data object of the player retrieved
     */
    public LiveData<Player> getPlayer(String playerId) {
        playerRepository.getPlayer(playerId, result -> {
            if (!result.getId().isEmpty()) {
                this.player.setValue(result);
            }
        });

        return this.player;
    }

    /**
     * Get a player object in the ViewModel. Called when player object is already retrieved
     *
     * @return A Live data object of the player
     */
    public LiveData<Player> getPlayer() {
        return this.player;
    }

    /**
     * Get all the qr codes the player have scanned
     *
     * @param player The player who scanned the qr codes
     * @return A live data of the qr codes
     */
    public LiveData<ArrayList<QRCode>> getScannedQRCodes(Player player) {
        qrCodeRepository.getScannedQRCodes(player, scannedQRCodes -> {
            this.scannedQRCodes.setValue(scannedQRCodes);
        });

        return this.scannedQRCodes;
    }


    public LiveData<QRCode> getHighestScore() {
        return this.highScoreQRCode;
    }

    public LiveData<QRCode> getLowestScore() {
        return this.lowScoreQRCode;
    }

    /**
     * Removes a QR code from the list of scanned QR codes for the given player and updates the
     * player's total score.
     *
     * @param qrCodeId The ID of the QR code to remove.
     * @param playerId The ID of the player who scanned the QR code.
     */
    public void removeScannedQRCode(String qrCodeId, String playerId) {
        // Update Firestore (reduce score and remove from qr code's playerIds)
        qrCodeRepository.removeQRCodeFromPlayer(qrCodeId, playerId);

        // Update this.scannedQRCodes
        ArrayList<QRCode> currentScannedQRCodes = this.scannedQRCodes.getValue();
        currentScannedQRCodes.removeIf(qrCode -> {
            if (Objects.equals(qrCode.getId(), qrCodeId)) {
                // Update total score as well
                Player currentPlayer = this.player.getValue();
                currentPlayer.setTotalScore(currentPlayer.getTotalScore() - qrCode.getScore());
                this.player.setValue(currentPlayer);

                return true;
            }

            return false;
        });

        this.scannedQRCodes.setValue(currentScannedQRCodes);
    }

    /**
     * Gets the list of all QR codes in the database
     * @return A live data object of the list of QR codes
     */
    public LiveData<List<QRCode>> getQRCodes(){
        LiveData<List<QRCode>> qrList = qrCodeRepository.getQRCodeList();
        return qrList;
    }
    public void addPhoneNumber(String playerID, String phoneNumber) {
        playerRepository.addPhoneNumber(playerID, phoneNumber);
    }


}