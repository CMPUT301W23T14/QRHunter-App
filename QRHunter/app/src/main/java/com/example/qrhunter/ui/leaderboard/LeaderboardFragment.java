package com.example.qrhunter.ui.leaderboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.UserRepository;
import com.example.qrhunter.databinding.FragmentLeaderboardBinding;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {
    private FragmentLeaderboardBinding binding;

    private LeaderboardViewModel leaderboardViewModel;

    private ListView recyclerView;

    private LeaderboardAdapter leaderboardAdapter;

    private ArrayList<Player> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserRepository userRepository = new UserRepository();
        dataList = new ArrayList<>();




        //Get ViewModel
        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        userRepository.getPlayers().addOnSuccessListener(queryDocumentSnapshots -> {
            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                Player player = queryDocumentSnapshots.getDocuments().get(i).toObject(Player.class);
                dataList.add(player);
//                Toast.makeText(getContext(), player.getId(), Toast.LENGTH_SHORT).show();
            }
            leaderboardAdapter.notifyDataSetChanged();
        });
        recyclerView = binding.leaderboardRecyclerView;
        leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList);
        recyclerView.setAdapter(leaderboardAdapter);
        leaderboardAdapter.notifyDataSetChanged();
        return binding.getRoot();
    }
}