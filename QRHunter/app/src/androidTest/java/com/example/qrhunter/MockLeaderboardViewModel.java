package com.example.qrhunter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;

import java.util.List;

public class MockLeaderboardViewModel extends LeaderboardViewModel {

    public MockLeaderboardViewModel(PlayerRepository playerRepository) {
        super(playerRepository);
    }

    @Override
    public LiveData<List<Player>> getPlayers() {
        // Return a LiveData object containing the fake players
        MutableLiveData<List<Player>> playersLiveData = new MutableLiveData<>();
        playersLiveData.setValue(getPlayerRepository().getFakePlayers());
        return playersLiveData;
    }
}
