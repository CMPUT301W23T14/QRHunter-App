package com.example.qrhunter.ui.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.databinding.FragmentAfterScanBinding;

public class AfterScanFragment extends Fragment {
    private FragmentAfterScanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAfterScanBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}