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

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModels
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Bind player info to texts
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        profileViewModel.getPlayer(deviceId).observe(getViewLifecycleOwner(), player -> {
            if (!player.getId().isEmpty()) {
                binding.username.setText(player.getUsername());
                binding.phoneNumberEditText.setText(player.getPhoneNumber());
                binding.totalScore.setText(Integer.toString(player.getTotalScore()));
                binding.rank.setText(Integer.toString(player.getRank()));
            }
        });

        // Get recycler view
        RecyclerView rvQRCodes = binding.qrCodeRecyclerView;

        // Sample qr code list, normally we would get the data from ViewModel instead
        ArrayList<QRCode> sampleQRCodes = new ArrayList<QRCode>() {
            {
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
                add(new QRCode("123", null, "SoloCrabMegaIce", 22.2, "", null));
            }
        };

        // Set up recycler view
        QRCodesAdapter qrCodesAdapter = new QRCodesAdapter(sampleQRCodes);
        rvQRCodes.setAdapter(qrCodesAdapter);
        rvQRCodes.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}