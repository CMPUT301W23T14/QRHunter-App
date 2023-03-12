package com.example.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;

import java.util.List;

public class LeaderboardViewModel extends ViewModel {
    private PlayerRepository playerRepository;
    private LiveData<List<Player>> players;

    public LeaderboardViewModel() {
        playerRepository = new PlayerRepository();
        players = playerRepository.getUsers();
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

}
