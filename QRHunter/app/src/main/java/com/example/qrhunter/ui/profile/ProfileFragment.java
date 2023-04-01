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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.data.model.QRCode;
import com.example.qrhunter.databinding.FragmentProfileBinding;
import com.example.qrhunter.ui.adapters.QRCodesAdapter;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.example.qrhunter.utils.PlayerUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Comparator;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Disposable phoneNumberText;

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

        phoneNumberText = RxTextView.textChanges(binding.phoneNumberEditText).debounce(1000, TimeUnit.MILLISECONDS).
                subscribe(phoneNumber -> {
                    profileViewModel.addPhoneNumber(deviceId, Objects.requireNonNull(binding.phoneNumberEditText.getText()).toString());
                });

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

                    // Sort by name. If we want uppercase first, remove .toLowerCase()
                    scannedQRCodes.sort(Comparator.comparing(qrCode -> qrCode.getName().toLowerCase()));

                    qrCodesAdapter.notifyDataSetChanged();
                    binding.scannedText.setText("(" + qrCodes.size() + ")");

                    if (qrCodes.isEmpty()) {
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
                        ProfileFragmentDirections.ActionNavigationProfileToQrCodeFragment action =
                                ProfileFragmentDirections.actionNavigationProfileToQrCodeFragment(lowScoreQRCode.getId());
                        navController.navigate(action);
                    });
                    binding.highestScoreContainer.setOnClickListener(v -> {
                        ProfileFragmentDirections.ActionNavigationProfileToQrCodeFragment action =
                                ProfileFragmentDirections.actionNavigationProfileToQrCodeFragment(highScoreQRCode.getId());
                        navController.navigate(action);
                    });
                });
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        phoneNumberText.dispose();
        super.onDestroyView();
        binding = null;
    }

}