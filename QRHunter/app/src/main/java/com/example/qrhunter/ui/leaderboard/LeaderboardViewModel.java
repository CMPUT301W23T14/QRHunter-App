package com.example.qrhunter.ui.leaderboard;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Model for LeaderboardFragment. Logic for getting, updating, and searching for players.
 */
public class LeaderboardViewModel extends ViewModel {
    private PlayerRepository playerRepository;
    private LiveData<List<Player>> players;

    public LeaderboardViewModel() {
        playerRepository = new PlayerRepository();
        players = playerRepository.getUsers();
    }

    /**
     * Gets the players in the leaderboard
     * @return The players in the leaderboard
     */
    public LiveData<List<Player>> getPlayers() {
        return players;
    }
    /**
     * Updates the players in the leaderboard
     * @return The players in the leaderboard
     */

    public LiveData<List<Player>> updatePlayers() {
        playerRepository = new PlayerRepository();
        players = playerRepository.getUsers();
        return players;
    }
    /**
     * Searches for players in the leaderboard
     * @param query The input by the user
     * @return The player(s) to be searched for
     */
    public LiveData<List<Player>> searchPlayers(String query) {

        MutableLiveData<List<Player>> filteredPlayers = new MutableLiveData<>();
        // If the query is empty or null, return the full list of players
        if (TextUtils.isEmpty(query)) {
            filteredPlayers.setValue(players.getValue()); // when initialized players = playerRepository.getUsers();
            return filteredPlayers;
        }
        // filter the list of players
        List<Player> filteredList = new ArrayList<>();
        for (Player player : Objects.requireNonNull(players.getValue())) { // enhanced for loop for lists
            if (player.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(player);
            }
        }
        // Set the filtered list as the value of filteredPlayers
        filteredPlayers.setValue(filteredList);
        return filteredPlayers;
    }
    /**
     * Setter for the playerRepository
     * @param playerRepository The playerRepository to be set
     */
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
}
