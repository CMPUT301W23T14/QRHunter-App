package com.example.qrhunter.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.Player;
import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        //Get ViewModels
        ProfileViewModel profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Get recycler view
        RecyclerView rvQRCodes = binding.qrCodeRecyclerView;

        // List of qr codes for the recycler view adapter
        ArrayList<QRCode> scannedQRCodes = new ArrayList<>();

        // Set up recycler view
        QRCodesAdapter qrCodesAdapter = new QRCodesAdapter(scannedQRCodes, false);

        qrCodesAdapter.setOnClickListeners(position -> {
            profileViewModel.removeScannedQRCode(scannedQRCodes.get(position).getId(), deviceId);
        });

        rvQRCodes.setAdapter(qrCodesAdapter);
        rvQRCodes.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Bind player info to texts
        profileViewModel.getPlayer(deviceId).observe(getViewLifecycleOwner(), player -> {
            if (!player.getId().isEmpty()) {
                binding.username.setText(player.getUsername());
                binding.phoneNumberEditText.setText(player.getPhoneNumber());
                binding.totalScore.setText(Double.toString(player.getTotalScore()));
                binding.rank.setText(Integer.toString(player.getRank()));

                profileViewModel.getScannedQRCodes(player).observe(getViewLifecycleOwner(), qrCodes -> {
                    scannedQRCodes.clear();
                    scannedQRCodes.addAll(qrCodes);
                    // sort by name
                    scannedQRCodes.sort(Comparator.comparing(qrCode -> qrCode.getName().toLowerCase())); // if we want uppercase first, remove .toLowerCase()
                    qrCodesAdapter.notifyDataSetChanged();
                    binding.scannedText.setText("(" + qrCodes.size() + ")");
                });
            }

        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}