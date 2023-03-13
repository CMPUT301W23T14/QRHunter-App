package com.example.qrhunter.ui.leaderboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.repository.PlayerRepository;
import com.example.qrhunter.databinding.FragmentLeaderboardBinding;

import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardFragment extends Fragment {
    private FragmentLeaderboardBinding binding;

    private LeaderboardViewModel leaderboardViewModel;

    private ListView listView;

    private LeaderboardAdapter leaderboardAdapter;

    private ArrayList<Player> dataList;

    private String deviceId;

    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        // Inflate the layout for this fragment
        deviceId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        listView = binding.leaderboardRecyclerView;

        getList();
        SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;
        // when user refreshes the layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (leaderboardAdapter != null) {
                leaderboardViewModel.updatePlayers().observe(getViewLifecycleOwner(), players -> {
                    dataList = new ArrayList<>(players);
                    leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList, deviceId);
                    listView.setAdapter(leaderboardAdapter);
                    Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                    leaderboardAdapter.notifyDataSetChanged();
                });
            }
            swipeRefreshLayout.setRefreshing(false);
        });



        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Toast.makeText(getContext(), "ID of clicked " + dataList.get(position).getId(), Toast.LENGTH_SHORT).show();
//            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_leaderboard_to_navigation_profile);
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (leaderboardAdapter != null) {
            leaderboardViewModel.updatePlayers().observe(getViewLifecycleOwner(), players -> {
                dataList = new ArrayList<>(players);
                leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList, deviceId);
                listView.setAdapter(leaderboardAdapter);
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                leaderboardAdapter.notifyDataSetChanged();
            });
        }
    }
    public void getList(){
        leaderboardViewModel.getPlayers().observe(getViewLifecycleOwner(), players -> {
            dataList = new ArrayList<>(players);
            leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList, deviceId);
            listView.setAdapter(leaderboardAdapter);
            leaderboardAdapter.notifyDataSetChanged();
        });
    }


}