package com.example.qrhunter.ui.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.databinding.FragmentScanBinding;

/**
 * This screen might make more sense as an activity
 */
public class ScanFragment extends Fragment {
    private FragmentScanBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}