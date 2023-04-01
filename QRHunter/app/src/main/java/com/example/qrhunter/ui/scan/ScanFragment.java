package com.example.qrhunter.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentScanBinding;
import com.google.zxing.Result;


/**
 * This screen might make more sense as an activity
 */
public class ScanFragment extends Fragment {
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    private FragmentScanBinding binding;
    private CodeScanner mCodeScanner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(requireActivity()).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false);

        //request permission to use the camera if permission is not granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        final Activity activity = getActivity();
        CodeScannerView scannerView = binding.scannerView;
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanViewModel.scanQRCode(result.getText());
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_scanFragment_to_afterScanFragment);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
