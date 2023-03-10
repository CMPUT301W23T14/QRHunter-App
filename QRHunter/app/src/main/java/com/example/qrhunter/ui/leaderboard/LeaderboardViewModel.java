package com.example.qrhunter.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.UserRepository;

import java.util.List;

public class LeaderboardViewModel extends ViewModel {
    private UserRepository userRepository;
    private LiveData<List<Player>> players;

    public LeaderboardViewModel() {
        userRepository = new UserRepository();
        players = userRepository.getUsers();

    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

}
