package com.example.qrhunter.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.UserRepository;


public class PlayerViewModel extends ViewModel {
    // Global states
    private final MutableLiveData<Player> player = new MutableLiveData<>();

    // State for user initialization
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<Boolean> validUsername = new MutableLiveData<>(false);
    UserRepository userRepository = new UserRepository();

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<Boolean> getValidUsername() {
        return validUsername;
    }

    public LiveData<Player> getPlayer() {
        return player;
    }

    public void updateUsername(String newUsername) {
        username.postValue(newUsername);

        userRepository.doesUsernameExist(newUsername, result -> {
            if (result || newUsername.isEmpty()) {
                validUsername.postValue(false);
            } else {
                validUsername.postValue(true);
            }
        });
    }

    public void createUser(String playerId, String username) {
        Player player = new Player(playerId, username);

        userRepository.createPlayer(player, result -> {
            this.player.setValue(player);
        });
    }
}