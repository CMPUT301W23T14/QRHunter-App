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
import com.example.qrhunter.utils.PlayerUtil;
import com.example.qrhunter.utils.QRCodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        //Get ViewModels
        ProfileViewModel profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        // Get List of all QR codes
        LiveData<List<QRCode>> qrList = profileViewModel.uniqueQRCodes();
        // Get the list of QR codes scanned by only one player
        qrList.observe(getViewLifecycleOwner(), qrCodes -> {
            HashMap<String, Double> scoreMap = new HashMap<>();
            HashMap<String, String> playerMap = new HashMap<>();
            for (QRCode qrCode : qrCodes) {
                if (qrCode.getPlayerIds().size() == 1){
                    scoreMap.put(qrCode.getId(), qrCode.getScore());
                    playerMap.put(qrCode.getId(), qrCode.getPlayerIds().get(0));
                }
            }
            // Sorting the score map
            HashMap<String, Double> sortedScoreMap = (HashMap<String, Double>) QRCodeUtil.sortByValue(scoreMap);
            HashMap<String, Integer> uniqueHighestQRCode = (HashMap<String, Integer>) QRCodeUtil.checkUniqueQRCode(deviceId, sortedScoreMap, playerMap);

            // Set the rank of the QR code for the current player
            for (QRCode qrCode : qrCodes) {
                if (uniqueHighestQRCode.containsKey(qrCode.getId()) && qrCode.isUnique()){
                    qrCode.setHighestUnique();
                    qrCode.setRank(uniqueHighestQRCode.get(qrCode.getId()));
                    Log.d("=========================", "onCreateView: "+qrCode.getId()+" "+qrCode.getRank());
                }
            }
        });

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
                    qrCodesAdapter.notifyDataSetChanged();
                    binding.scannedText.setText("(" + qrCodes.size() + ")");

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
                /*// get all qr codes from players
                // make a hash score, playerList
                // sort hash score for playerlist of size 1
                // get the player id from the player list
                // display it in the player profile
                LiveData<List<QRCode>> qrList = profileViewModel.uniqueQRCodes();

                qrList.observe(getViewLifecycleOwner(), qrCodes -> {
                    HashMap<String, Double> scoreMap = new HashMap<>();
                    HashMap<String, String> playerMap = new HashMap<>();
                    for (QRCode qrCode : qrCodes) {
                        if (qrCode.getPlayerIds().size() == 1){
                            scoreMap.put(qrCode.getId(), qrCode.getScore());
                            playerMap.put(qrCode.getId(), qrCode.getPlayerIds().get(0));
                        }
                    }
                    HashMap<String, Double> sortedScoreMap = (HashMap<String, Double>) QRCodeUtil.sortByValue(scoreMap);
                    int count = 0;
                    for (Map.Entry<String, Double> entry : sortedScoreMap.entrySet()) {
                        if (entry.getKey().equals(playerMap.get(entry.getKey())) && deviceId.equals(playerMap.get(entry.getKey()))){
                        }
                        count++;
                    }
                });*/




        });





        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}