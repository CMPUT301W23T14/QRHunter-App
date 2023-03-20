package com.example.qrhunter.ui.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrhunter.R;
import com.example.qrhunter.databinding.FragmentAfterScanBinding;
import com.example.qrhunter.utils.QRCodeUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * screen segment after scan
 */
public class AfterScanFragment extends Fragment {
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1001;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private FragmentAfterScanBinding binding;
    private boolean locationPermissionGranted;
    private Bitmap savedPhoto;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    locationPermissionGranted = true;
                }
            }
    );

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get ViewModel
        ScanViewModel scanViewModel = new ViewModelProvider(requireActivity()).get(ScanViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAfterScanBinding.inflate(inflater, container, false);

        // request location
        if (!locationPermissionGranted) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // Bind data to ui
        String hashValue = scanViewModel.getQRCodeHash().getValue();

        binding.QRVisual.setText(QRCodeUtil.generateVisualRepresentation(hashValue));
        binding.QRName.setText(QRCodeUtil.generateName(hashValue));
        binding.QRCodeScoretext.setText(QRCodeUtil.generateScore(hashValue) + " Points");

        binding.qrBackButton.setOnClickListener(view -> {
            scanViewModel.clearQRCode();
            Navigation.findNavController(view).popBackStack();
        });

        binding.addGeoLocationButton.setOnClickListener(view -> {

            if (binding.addGeoLocationButton.isChecked() && locationPermissionGranted) {
                try {
                    LocationManager lm = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (gps_enabled) {
                        Location lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastKnownLocation != null) {
                            System.out.println(binding.addGeoLocationButton.isChecked());
                            scanViewModel.setGeolocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }
                } catch (SecurityException e) {
                    Toast.makeText(view.getContext(), "Could not get your location!", Toast.LENGTH_SHORT).show();
                }
            } else {
                scanViewModel.setGeolocation(0, 0);
            }

        });

        // set to get the image after taking the photo
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle bundle = data.getExtras();
                            savedPhoto = (Bitmap) bundle.get("data");
                            savedPhoto = Bitmap.createScaledBitmap(savedPhoto, 640, 480, true);
                            //Bitmap finalPhoto = Bitmap.createScaledBitmap(savedPhoto, 640, 480, true);
                            scanViewModel.setPhotoLocation(savedPhoto);

                            binding.locationImage.setVisibility(View.VISIBLE);
                            binding.addPhotoLocationButton.setImageResource(R.drawable.remove_icon);
                            binding.locationImage.setImageBitmap(savedPhoto);

                        }
                    }
                });

        // open the camera functionality and take a picture of the location
        binding.addPhotoLocationButton.setOnClickListener(view -> {
            //request permission to use the camera if permission is not granted
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }

            // we delete the picture
            if (!scanViewModel.getLocation().getValue().photos.isEmpty()) {
                scanViewModel.setPhotoLocation(null);
                binding.addPhotoLocationButton.setImageResource(R.drawable.add_icon);
                binding.locationImage.setVisibility(View.GONE);
            } else {
                // we are not deleting the photo but taking a picture
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(intent);
            }
        });

        binding.saveButton.setOnClickListener(view -> {
            // use the model to add the qrcode
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

            if (savedPhoto != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                savedPhoto.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] b = baos.toByteArray();
                scanViewModel.completeScan(deviceId, b);
            }
            else {
                scanViewModel.completeScan(deviceId, null);
            }

            // navigate to somewhere after this is done
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_after_scan_to_navigation_map);

        });

        return binding.getRoot();
    }

}