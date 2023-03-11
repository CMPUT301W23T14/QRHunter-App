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
        ScanViewModel scanViewModel = new ViewModelProvider(requireActivity()).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAfterScanBinding.inflate(inflater, container, false);

        scanViewModel.getQRCodeContent().observe(requireActivity(), newQRCodeContent -> {
            binding.qrContent.setText(newQRCodeContent);
        });

        //TODO: Add an app bar like the one in Figma
        
        return binding.getRoot();
    }


}