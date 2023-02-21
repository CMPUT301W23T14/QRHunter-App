package com.example.qrhunter.ui.map;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentLeaderboardBinding;
import com.example.qrhunter.databinding.FragmentMapBinding;
import com.example.qrhunter.ui.leaderboard.LeaderboardViewModel;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        MapViewModel mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

}