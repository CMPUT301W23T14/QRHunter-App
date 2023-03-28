package com.example.qrhunter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;

import java.util.List;

public class MockPlayerRepository extends PlayerRepository {
    private List<Player> fakePlayers;

    public void setFakePlayers(List<Player> fakePlayers) {
        this.fakePlayers = fakePlayers;
    }

    @Override
    public LiveData<List<Player>> getUsers() {
        MutableLiveData<List<Player>> usersLiveData = new MutableLiveData<>();
        usersLiveData.setValue(fakePlayers);
        return usersLiveData;
    }
}
