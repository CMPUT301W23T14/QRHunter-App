package com.example.qrhunter.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;
import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<String> rankQRCode = new ArrayList<>();

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

                    LiveData<List<QRCode>> qrCodesLiveData = profileViewModel.getQRCodes();
                    qrCodesLiveData.observe(getViewLifecycleOwner(), qrCodes1 -> {
                        HashMap<String, Double> qrCodeMap = new HashMap<>();
                        HashMap<String, String> idMap = new HashMap<>();
                        for (QRCode qrCode : qrCodes1) {
                            if (qrCode.getPlayerIds().size() == 1) {
                                qrCodeMap.put(qrCode.getId(), qrCode.getScore());
                                idMap.put(qrCode.getId(), qrCode.getPlayerIds().get(0));
                            }
                        }
                        HashMap<String, Double> sortByValue = (HashMap<String, Double>) QRCodeUtil.sortByValue(qrCodeMap);
                        List<String> temp = QRCodeUtil.checkUniqueQRCode(deviceId, sortByValue, idMap);
                        rankQRCode.clear();
                        rankQRCode.addAll(temp);

                        for (QRCode qrCode : qrCodes) {
                            if (rankQRCode.contains(qrCode.getId()) && qrCode.isUnique()) {
                                qrCode.setRank(Integer.parseInt(rankQRCode.get(1)));
                            }
                        }
                        scannedQRCodes.clear();
                        scannedQRCodes.addAll(qrCodes);
                        qrCodesAdapter.notifyDataSetChanged();
                        binding.scannedText.setText("(" + qrCodes.size() + ")");
                    });
                });
            }
                // Display highest score
                profileViewModel.getHighestScore().observe(getViewLifecycleOwner(), highScore -> {
                    binding.highestScore.setText(Double.toString(highScore));
                });
                // Display lowest score
                profileViewModel.getLowestScore().observe(getViewLifecycleOwner(), lowScore -> {
                    binding.lowestScore.setText(Double.toString(lowScore));
            });


        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}