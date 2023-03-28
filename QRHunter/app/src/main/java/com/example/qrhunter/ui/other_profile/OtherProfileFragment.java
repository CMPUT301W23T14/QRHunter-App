package com.example.qrhunter.ui.other_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentOtherProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;

import java.util.ArrayList;

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

        // Get player id from destination argument
        String deviceId = OtherProfileFragmentArgs.fromBundle(getArguments()).getPlayerId();

        // Get recycler view
        RecyclerView rvQRCodes = binding.qrCodeRecyclerView;

        ArrayList<QRCode> scannedQRCodes = new ArrayList<>();

        // Set up recycler view
        QRCodesAdapter qrCodesAdapter = new QRCodesAdapter(scannedQRCodes, true);

        qrCodesAdapter.setOnClickListeners(position -> {
            //profileViewModel.removeScannedQRCode(scannedQRCodes.get(position).getId(), deviceId);
        });

        rvQRCodes.setAdapter(qrCodesAdapter);
        rvQRCodes.setLayoutManager(new LinearLayoutManager(requireContext()));


        // Bind player info to texts
        otherProfileViewModel.getPlayer(deviceId).observe(getViewLifecycleOwner(), player -> {
            if (!player.getId().isEmpty()) {
                binding.username.setText(player.getUsername());
                binding.phoneNumberEditText.setText(player.getPhoneNumber());
                binding.totalScore.setText(Double.toString(player.getTotalScore()));
                binding.rank.setText(Integer.toString(player.getRank()));

                otherProfileViewModel.getScannedQRCodes(player).observe(getViewLifecycleOwner(), qrCodes -> {
                    scannedQRCodes.clear();
                    scannedQRCodes.addAll(qrCodes);
                    qrCodesAdapter.notifyDataSetChanged();
                    binding.scannedText.setText("(" + qrCodes.size() + ")");
                });
            }

        });

        binding.otherPlayerActionBarBackButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}