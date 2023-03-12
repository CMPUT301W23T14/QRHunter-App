package com.example.qrhunter.ui.qrcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrhunter.databinding.FragmentQrCodeBinding;

public class QrCodeFragment extends Fragment {

    FragmentQrCodeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel, using activities' lifecycle because the state in
        QrCodeViewModel qrCodeViewModel = new ViewModelProvider(this).get(QrCodeViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentQrCodeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


}