package com.example.qrhunter.ui.other_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentOtherProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;
import com.example.qrhunter.utils.PlayerUtil;

import java.util.ArrayList;
import java.util.Comparator;

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

        rvQRCodes.setAdapter(qrCodesAdapter);
        rvQRCodes.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Bind player info to texts
        otherProfileViewModel.getPlayer(deviceId).observe(getViewLifecycleOwner(), player -> {
            if (!player.getId().isEmpty()) {
                binding.username.setText(player.getUsername());
                binding.phoneNumberEditText.setText(player.getPhoneNumber());
                binding.totalScore.setText(Double.toString(player.getTotalScore()));

                otherProfileViewModel.getScannedQRCodes(player).observe(getViewLifecycleOwner(), qrCodes -> {
                    scannedQRCodes.clear();
                    scannedQRCodes.addAll(qrCodes);

                    // Sort by name. If we want uppercase first, remove .toLowerCase()
                    scannedQRCodes.sort(Comparator.comparing(qrCode -> qrCode.getName().toLowerCase()));

                    if (qrCodes.isEmpty()) {
                        binding.lowestScore.setText("0");
                        binding.highestScore.setText("0");
                        binding.lowestScoreContainer.setOnClickListener(null);
                        binding.highestScoreContainer.setOnClickListener(null);
                        return;
                    }

                    // Lowest and highest scoring qr code
                    QRCode lowScoreQRCode = PlayerUtil.calculateLowestScoreQRCode(qrCodes);
                    QRCode highScoreQRCode = PlayerUtil.calculateHighestScoreQRCode(qrCodes);


                    binding.lowestScore.setText(Double.toString(lowScoreQRCode.getScore()));
                    binding.highestScore.setText(Double.toString(highScoreQRCode.getScore()));

                    // Set navigation to lowest and highest scoring qr code
                    NavController navController = NavHostFragment.findNavController(this);
                    binding.lowestScoreContainer.setOnClickListener(v -> {
                        com.example.qrhunter.ui.other_profile.OtherProfileFragmentDirections.ActionOtherProfileFragmentToQrCodeFragment action =
                                OtherProfileFragmentDirections.actionOtherProfileFragmentToQrCodeFragment(lowScoreQRCode.getId());
                        navController.navigate(action);
                    });
                    binding.highestScoreContainer.setOnClickListener(v -> {
                        com.example.qrhunter.ui.other_profile.OtherProfileFragmentDirections.ActionOtherProfileFragmentToQrCodeFragment action =
                                OtherProfileFragmentDirections.actionOtherProfileFragmentToQrCodeFragment(highScoreQRCode.getId());
                        navController.navigate(action);
                    });

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