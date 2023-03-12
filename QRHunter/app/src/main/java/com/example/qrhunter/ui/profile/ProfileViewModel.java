package com.example.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private PlayerRepository playerRepository = new PlayerRepository();


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
}