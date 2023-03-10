package com.example.qrhunter.ui.leaderboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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

        //Get ViewModel
        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        leaderboardViewModel.getPlayers().observe(getViewLifecycleOwner(), players -> {
            dataList = new ArrayList<>(players);
            // some dummy values to fill the list
            dataList.add(new Player("gweiujh", "wihbjnge", "123124", 12, 124124));
            dataList.add(new Player("gweiu21jh", "wihbj124ge", "123124", 12, 124124));
            dataList.add(new Player("gweiu23jh", "wihbj124nge", "123124", 12, 124124));
            dataList.add(new Player("gwei512ujh", "GeneralFranky", "12345", 112, 124124));
            dataList.add(new Player("gweiu41jh", "wihb132jnge", "123124", 12, 124124));
            dataList.add(new Player("gwei23ujh", "wihb4jnge", "123124", 12, 12345));
            dataList.add(new Player("gwei412ujh", "wih1bjnge", "123124", 12, 124124));
            dataList.add(new Player("gwe4iujh", "wihbj23nge", "123124", 12, 124124));
            // remove this part later

            leaderboardAdapter = new LeaderboardAdapter(getContext(), dataList);
            recyclerView.setAdapter(leaderboardAdapter);
            leaderboardAdapter.notifyDataSetChanged();
        });




        recyclerView = binding.leaderboardRecyclerView;
        recyclerView.setOnItemClickListener((parent, view, position, id) -> {
//            Toast.makeText(getContext(), "ID of clicked " + dataList.get(position).getId(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_leaderboard_to_navigation_profile);
        });


        return binding.getRoot();
    }
}