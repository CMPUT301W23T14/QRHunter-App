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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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

    private ImageView refreshButton;

    private SearchView searchView;
    @SuppressLint("HardwareIds")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        // Inflate the layout for this fragment
        deviceId = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        listView = binding.leaderboardListView;
        refreshButton = binding.leaderboardRefreshButton;

        getList();
        // gets the updated list and plays an animation
        refreshButton.setOnClickListener(v -> {
            if (leaderboardAdapter != null) {
                leaderboardViewModel.updatePlayers().observe(getViewLifecycleOwner(), players -> {
                    dataList = new ArrayList<>(players);
                    leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList, deviceId);
                    listView.setAdapter(leaderboardAdapter);
                    leaderboardAdapter.notifyDataSetChanged();
                });
                RotateAnimation rotateAnimation = new RotateAnimation(
                        0, 360,  // From and to angles (in degrees)
                        Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point X
                        Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point Y
                );
                rotateAnimation.setDuration(500); // Duration in milliseconds
                refreshButton.startAnimation(rotateAnimation);
                Toast.makeText(getContext(), "Leaderboard Updated", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: navigate to profile fragment
        listView.setOnItemClickListener((parent, view, position, id) -> {
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