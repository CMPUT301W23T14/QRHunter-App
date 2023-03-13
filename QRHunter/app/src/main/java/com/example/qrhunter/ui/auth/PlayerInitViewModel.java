package com.example.qrhunter.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;


public class PlayerInitViewModel extends ViewModel {
    private final MutableLiveData<Player> player = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<Boolean> validUsername = new MutableLiveData<>(false);
    PlayerRepository playerRepository = new PlayerRepository();
    /**
     * Returns a LiveData object containing the username string.
     *
     * @return A LiveData object containing the username string.
     */
    public LiveData<String> getUsername() {
        return username;
    }


    /**
     * Returns a LiveData object indicating whether the current username is valid or not.
     *
     * @return A LiveData object indicating whether the current username is valid or not.
     */
    public LiveData<Boolean> getValidUsername() {
        return validUsername;
    }

    /**
     * Returns a LiveData object containing the current player data.
     *
     * @return A LiveData object containing the current player data.
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