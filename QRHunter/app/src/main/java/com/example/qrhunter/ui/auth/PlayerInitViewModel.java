package com.example.qrhunter.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;

/**
 * Model for PlayerInitFragment which stores the player, username, and validity of the username.
 */

public class PlayerInitViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<Boolean> validUsername = new MutableLiveData<>(false);
    PlayerRepository playerRepository = new PlayerRepository();

    /**
     * Gets the username of the player
     * @return The username of the player
     */
    public LiveData<String> getUsername() {
        return username;
    }

    /**
     * Gets to check if the username of a player is valid
     * @return The validity of a player's username
     */
    public LiveData<Boolean> getValidUsername() {
        return validUsername;
    }

    /**
     * Gets the player
     * @return The player
     */
    public LiveData<Player> getPlayer() {
        return player;
    }

    /**
     * Called when user changes the text in username field.
     * Also updates the validUsername state
     *
     * @param newUsername The new username that user typed
     */
    public void updateUsername(String newUsername) {
        username.postValue(newUsername);

        playerRepository.doesUsernameExist(newUsername, result -> {
            if (result || newUsername.isEmpty()) {
                validUsername.postValue(false);
            } else {
                validUsername.postValue(true);
            }
        });
    }

    /**
     * Creates a player in Firestore
     *
     * @param playerId The player id, which is also the device ID
     * @param username The player's username, which is unique
     */
    public void createPlayer(String playerId, String username) {
        Player player = new Player(playerId, username);

        playerRepository.createPlayer(player, result -> {
            this.player.setValue(player);
        });
    }
}