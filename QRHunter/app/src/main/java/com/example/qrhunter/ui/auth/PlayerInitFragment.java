package com.example.qrhunter.ui.auth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentPlayerInitBinding;
import com.example.qrhunter.ui.profile.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlayerInitFragment extends Fragment {

    private FragmentPlayerInitBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentPlayerInitBinding.inflate(inflater, container, false);

        // Hide navigation bar
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);

        return binding.getRoot();
    }


}