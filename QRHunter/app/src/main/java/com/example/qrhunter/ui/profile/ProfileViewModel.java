package com.example.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<QRCode>> scannedQRCodes = new MutableLiveData<>();
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
}