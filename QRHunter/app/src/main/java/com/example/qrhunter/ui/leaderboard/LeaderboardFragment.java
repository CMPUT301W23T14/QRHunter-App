package com.example.qrhunter.ui.leaderboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.databinding.FragmentLeaderboardBinding;

import java.util.ArrayList;

/**
 * Fragment used to display the leaderboard. Also can utilize a search bar for specific players in the leaderboard.
 */

public class LeaderboardFragment extends Fragment {
    public LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;
    private ListView listView;

    private LeaderboardAdapter leaderboardAdapter;

    private ArrayList<Player> dataList;

    private String deviceId;

    private ImageView refreshButton;
    private Handler handler = new Handler();
    private Runnable runnable;


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
        searchView = binding.leaderboardSearchUser;
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

        // this is the search bar logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something when the user submits the search query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Delay the search query by 500 milliseconds
                // input debounce source: https://stackoverflow.com/questions/34955109/throttle-onquerytextchange-in-searchview
                handler.removeCallbacks(runnable); // Remove any previously scheduled searches
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        leaderboardViewModel.searchPlayers(newText).observe(getViewLifecycleOwner(), players -> {
                            // Update the data list in the adapter
                            dataList.clear();
                            dataList.addAll(players);
                            leaderboardAdapter.notifyDataSetChanged();
                        });
                    }
                };
                handler.postDelayed(runnable, 500); // Schedule the new search query
                // Return true to indicate that the query has been handled
                return true;
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

    public void getList() {
        leaderboardViewModel.getPlayers().observe(getViewLifecycleOwner(), players -> {
            dataList = new ArrayList<>(players);
            leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList, deviceId);
            listView.setAdapter(leaderboardAdapter);
            leaderboardAdapter.notifyDataSetChanged();
        });
    }


}