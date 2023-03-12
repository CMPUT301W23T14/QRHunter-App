package com.example.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.data.repository.QRCodeRepository;

import java.util.ArrayList;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<QRCode>> scannedQRCodes = new MutableLiveData<>();
    private PlayerRepository playerRepository = new PlayerRepository();
    private QRCodeRepository qrCodeRepository = new QRCodeRepository();


    /**
     * Gets a player object
     *
     * @param playerId The id of the player
     */
    public LiveData<Player> getPlayer(String playerId) {
        playerRepository.getPlayer(playerId, result -> {
            if (!result.getId().isEmpty()) {
                this.player.setValue(result);
            }
        });

        return this.player;
    }

    public LiveData<ArrayList<QRCode>> getScannedQRCodes(Player player) {
        qrCodeRepository.getScannedQRCodes(player, scannedQRCodes -> {
            this.scannedQRCodes.setValue(scannedQRCodes);
        });

        return this.scannedQRCodes;
    }
}