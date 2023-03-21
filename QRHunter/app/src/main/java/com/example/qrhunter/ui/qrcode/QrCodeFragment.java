package com.example.qrhunter.ui.qrcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

        // Get qr code id from destination argument
        String qrCodeId = QrCodeFragmentArgs.fromBundle(getArguments()).getQrCodeId();

        // Bind data to ui
        qrCodeViewModel.getQRCode(qrCodeId)
                .observe(getViewLifecycleOwner(), qrCode -> {
                    if (qrCode != null) {
                        binding.QRName.setText(qrCode.getName());
                        binding.QRCodeScoretext.setText(Double.toString(qrCode.getScore()));
                        binding.QRVisual.setText(qrCode.getVisualRepresentation());

                        // set the address
                        binding.QRCodeAddresstext.setText(qrCodeViewModel.getAddress(qrCode, getContext()));

                        qrCodeViewModel.getScannedBy(qrCode).observe(getViewLifecycleOwner(), amountScannedBy -> {
                            if (amountScannedBy > 0) {
                                binding.scannedByText.setText(amountScannedBy.toString() + " players");
                            }
                        });
                    }
                });


        binding.qrBackButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });

        return binding.getRoot();
    }


}