package com.example.qrhunter.ui.other_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.databinding.FragmentOtherProfileBinding;

/**
 * This fragment is for viewing other players' profile page
 */
public class OtherProfileFragment extends Fragment {

    private FragmentOtherProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModels
        OtherProfileViewModel otherProfileViewModel = new ViewModelProvider(this).get(OtherProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentOtherProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

}